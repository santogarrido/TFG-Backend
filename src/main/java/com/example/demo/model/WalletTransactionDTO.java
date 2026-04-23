package com.example.demo.model;

import java.math.BigDecimal;

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

	private String description;

	public WalletTransactionDTO(long walletId, int userId, int facilityId, WalletReferenceType referenceType,
			Long referenceId, BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter,
			String description) {
		super();
		this.walletId = walletId;
		this.userId = userId;
		this.facilityId = facilityId;
		this.referenceType = referenceType;
		this.referenceId = referenceId;
		this.amount = amount;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
		this.description = description;
	}
	
	
}
