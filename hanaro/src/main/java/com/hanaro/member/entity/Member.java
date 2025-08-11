package com.hanaro.member.entity;

import org.hibernate.annotations.ColumnDefault;

import com.hanaro.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	@ColumnDefault("'익명'")
	private String nickname;

	@Column(nullable = false, length = 50, unique = true)
	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private MemberRole role = MemberRole.ROLE_USER;

	// 탈퇴일, 성별, 생년월일(나이) 추가할 수도 안 할 수도
}
