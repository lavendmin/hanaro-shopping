package com.hanaro.item.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.hanaro.SearchCond;
import com.hanaro.item.dto.ItemDTO;
import com.hanaro.item.dto.ItemRequestDTO;
import com.hanaro.item.dto.ItemResponseDTO;

public interface ItemService {
	ItemResponseDTO createItem(List<MultipartFile> files, ItemRequestDTO itemRequestDTO);

	Page<ItemResponseDTO> getItems(SearchCond searchCond);

	ItemResponseDTO updateItem(long id, ItemDTO itemDTO);

	String deleteItem(long id);
}
