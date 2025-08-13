package com.hanaro.item.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.hanaro.SearchCond;
import com.hanaro.item.dto.ItemDTO;
import com.hanaro.item.dto.ItemRequestDTO;
import com.hanaro.item.dto.ItemResponseDTO;
import com.hanaro.item.entity.Item;

public interface ItemService {
	ItemResponseDTO createItem(List<MultipartFile> files, ItemRequestDTO itemRequestDTO);

	Page<ItemDTO> getItems(SearchCond searchCond);

	ItemResponseDTO updateItem(Long id, ItemDTO itemDTO);

	String deleteItem(Long id);

	ItemDTO toItemDTO(Item item);

	ItemResponseDTO getItemById(Long id);
}
