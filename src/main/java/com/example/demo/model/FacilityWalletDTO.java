package com.example.demo.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityWalletDTO {

    private int id;

    private int facilityId;

    private BigDecimal amount;

	public FacilityWalletDTO(int facilityId, BigDecimal amount) {
		super();
		this.facilityId = facilityId;
		this.amount = amount;
	}
    
    
    
}
