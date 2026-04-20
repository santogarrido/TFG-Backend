package com.example.demo.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {

	private int id;
	
	private int userId;
	
	private int facilityId;
	
	private BigDecimal money;

	public WalletDTO(int userId, int facilityId, BigDecimal money) {
		super();
		this.userId = userId;
		this.facilityId = facilityId;
		this.money = money;
	}


	
	
	
}
