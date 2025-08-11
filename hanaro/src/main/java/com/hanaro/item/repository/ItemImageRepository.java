package com.hanaro.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanaro.item.entity.ItemImage;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}
