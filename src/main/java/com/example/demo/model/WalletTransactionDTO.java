package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.entity.WalletReferenceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionDTO {

	private long id;

	private long walletId;

	private int userId;

	private int facilityId;

	private WalletReferenceType referenceType;

	private Long referenceId;

	private BigDecimal amount;

	private BigDecimal balanceBefore;

	private BigDecimal balanceAfter;

	private LocalDateTime createdAt;

	private String description;
}
