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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;

	@Value("${upload.path}")
	private String uploadPath = "src/main/resources";

	@Override
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
	public Page<ItemResponseDTO> getItems(SearchCond searchCond) {
		Pageable pageable = searchCond.getPageable();

		Page<Item> items;
		if (searchCond.needSearch()) {
			String searchTerm = searchCond.getSearchTerm();
			items = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm,
				searchTerm, pageable);
		} else {
			items = itemRepository.findAll(pageable);
		}

		return items.map(ItemServiceImpl::toItemResponseDTO);
	}

	@Override
	public ItemResponseDTO updateItem(long id, ItemDTO itemDTO) {
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
	public String deleteItem(long id) {
		Item item = itemRepository.findById(id).orElse(null);

		if (item == null) {
			return "해당 상품을 찾을 수 없습니다.";
		}

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

	private String getTodayPath() {
		LocalDateTime now = LocalDateTime.now();
		return String.format("%4d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
	}
}
