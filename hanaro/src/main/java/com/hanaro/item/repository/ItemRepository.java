package com.hanaro.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hanaro.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	Page<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description,
		Pageable pageable);
}
