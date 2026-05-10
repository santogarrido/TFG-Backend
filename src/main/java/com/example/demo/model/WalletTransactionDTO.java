package com.example.demo.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionDTO {

	private int id;

	private int walletId;

	private BigDecimal amount;

	private BigDecimal balanceBefore;

	private BigDecimal balanceAfter;

	private String description;

	public WalletTransactionDTO(int walletId, BigDecimal amount, 
			BigDecimal balanceBefore, BigDecimal balanceAfter, String description) {
		super();
		this.walletId = walletId;
		this.amount = amount;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
		this.description = description;
	}
	
	
}
