package com.hanaro.cart.entity;

import com.hanaro.BaseEntity;
import com.hanaro.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class Cart extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "customer", foreignKey = @ForeignKey(
		name = "fk_Cart_customer_Member",
		foreignKeyDefinition = "FOREIGN KEY (customer) REFERENCES Member(id) ON DELETE CASCADE"))
	private Member customer;
}
