package com.hanaro.item.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import com.hanaro.RepositoryTest;
import com.hanaro.item.entity.Item;

class ItemRepositoryTest extends RepositoryTest {
	@Autowired
	ItemRepository itemRepository;

	@Test
	@Commit
	void addTest() {
		long preCount = itemRepository.count();

		List<Item> items = Stream.iterate(1, n -> n + 1).limit(20)
			.map(n -> Item.builder()
				.name("item" + n)
				.description("description" + n)
				.stock(30)
				.price(10000)
				.build()
			).toList();

		itemRepository.saveAll(items);

		assertEquals(preCount + 20, itemRepository.count());
	}
}
