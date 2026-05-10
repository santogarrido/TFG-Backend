package com.example.demo.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWalletDTO {
	
    private int id;

    private int userId;

    private BigDecimal amount;

	public UserWalletDTO(int userId, BigDecimal amount) {
		super();
		this.userId = userId;
		this.amount = amount;
	}
    
    
}
