package com.hanaro.item.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hanaro.SearchCond;
import com.hanaro.item.dto.ItemDTO;
import com.hanaro.item.dto.ItemImageDTO;
import com.hanaro.item.dto.ItemRequestDTO;
import com.hanaro.item.dto.ItemResponseDTO;
import com.hanaro.item.entity.Item;
import com.hanaro.item.entity.ItemImage;
import com.hanaro.item.repository.ItemImageRepository;
import com.hanaro.item.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemServiceImpl implements ItemService {
	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;

	@Value("${upload.path}")
	private String uploadPath = "src/main/resources";

	@Override
	@Transactional
	public ItemResponseDTO createItem(List<MultipartFile> files, ItemRequestDTO itemRequestDTO) {
		Item item = itemRequestDTO.toEntity();
		itemRepository.save(item);

		List<ItemImageDTO> itemImageDTOList = new ArrayList<>();

		if (files != null) {
			files.forEach(file -> {
				String orgFname = file.getOriginalFilename();
				String uuid = UUID.randomUUID().toString();
				String savedFname = uuid + "_" + orgFname;

				try {
					String savedir = getTodayPath();
					Path uploadDir = Paths.get(uploadPath + File.separator + savedir);
					Path upfilePath = Paths.get(uploadPath + File.separator + savedir + File.separator + savedFname);
					if (!Files.exists(uploadDir)) {
						Files.createDirectories(uploadDir);
					}
					file.transferTo(upfilePath);

					itemImageDTOList.add(ItemImageDTO.builder()
						.orgname(orgFname)
						.savename(savedFname)
						.savedir(savedir)
						.itemId(item.getId())
						.build());

				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}

		// itemImage DTO 리스트를 엔티티로
		List<ItemImage> itemImages = itemImageDTOList.stream().map(ItemImageDTO::toEntity).toList();
		itemImages.forEach(itemImage -> itemImage.setItem(item));
		item.setImages(itemImages);
		itemImageRepository.saveAll(itemImages);

		return toItemResponseDTO(item);
	}

	@Override
	public Page<ItemDTO> getItems(SearchCond searchCond) {
		Pageable pageable = searchCond.getPageable();

		Page<Item> items;
		if (searchCond.needSearch()) {
			String searchTerm = searchCond.getSearchTerm();
			items = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm,
				searchTerm, pageable);
		} else {
			items = itemRepository.findAll(pageable);
		}

		return items.map(this::toItemDTO);
	}

	@Override
	@Transactional
	public ItemResponseDTO updateItem(Long id, ItemDTO itemDTO) {
		Item item = itemRepository.findById(id).orElseThrow();

		item.setName(itemDTO.getName());
		item.setDescription(itemDTO.getDescription());
		item.setStock(itemDTO.getStock());
		item.setPrice(itemDTO.getPrice());
		item.setDiscount(itemDTO.getDiscount());

		itemRepository.save(item);

		return toItemResponseDTO(item);
	}

	@Override
	@Transactional
	public String deleteItem(Long id) {
		Item item = itemRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다.")
		);

		// resources에 있는 이미지 파일 삭제
		item.getImages().forEach(image ->
			deleteImageFile(image.getSavedir(), image.getSavename()));

		itemRepository.delete(item);

		return "해당 상품을 삭제하였습니다.";
	}

	@Override
	public ItemDTO toItemDTO(Item item) {
		return ItemDTO.builder()
			.name(item.getName())
			.description(item.getDescription())
			.stock(item.getStock())
			.price(item.getPrice())
			.discount(item.getDiscount())
			.build();
	}

	@Override
	public ItemResponseDTO getItemById(Long id) {
		Item item = itemRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다.")
		);
		return toItemResponseDTO(item);
	}

	public static ItemResponseDTO toItemResponseDTO(Item item) {
		List<ItemImageDTO> imageDTOs = item.getImages() == null
			? List.of()
			: item.getImages().stream().map(ItemImageDTO::new).toList();

		return ItemResponseDTO.builder()
			.id(item.getId())
			.name(item.getName())
			.description(item.getDescription())
			.stock(item.getStock())
			.price(item.getPrice())
			.discount(item.getDiscount())
			.images(imageDTOs)
			.build();
	}

	private void deleteImageFile(String savedir, String savename) {
		try {
			Path filePath = Paths.get(uploadPath, savedir, savename);
			if (Files.exists(filePath)) {
				Files.delete(filePath);
				log.info("Deleted image file: {}", filePath);
			} else {
				log.info("File not found, skip delete: {}", filePath);
			}
		} catch (Exception e) {
			log.error("Failed to delete image file: {}/{}", savedir, savename);
		}
	}

	private String getTodayPath() {
		LocalDateTime now = LocalDateTime.now();
		return String.format("%4d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
	}
}
