package com.example.demo.service;

import java.math.BigDecimal;

import com.example.demo.model.WalletDTO;

public interface WalletService {

	WalletDTO getUserWallet (long id);
	
	WalletDTO getFacilityWallet(long id);
	
	void debitUserWallet(long userId, BigDecimal amount);
	
	void creditUserWallet(long userId, BigDecimal amount);
	
	void creditFacilityWallet(long facilityId, BigDecimal amount);
	
	void debitFacilityWallet(long facilityId, BigDecimal amount);
	
}
