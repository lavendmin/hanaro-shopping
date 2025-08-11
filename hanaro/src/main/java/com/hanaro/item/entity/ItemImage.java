package com.hanaro.item.entity;

import com.hanaro.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orgname;

	private String savename;

	private String savedir;

	@ManyToOne()
	@JoinColumn(
		name = "item",
		foreignKey = @ForeignKey(
			name = "fk_ItemImage_item",
			foreignKeyDefinition = "FOREIGN KEY (item) REFERENCES Item(id) ON DELETE CASCADE ON UPDATE CASCADE"))
	private Item item;
}
