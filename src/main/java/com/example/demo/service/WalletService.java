package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.model.FacilityWalletDTO;
import com.example.demo.model.UserWalletDTO;
import com.example.demo.model.WalletTransactionDTO;

public interface WalletService {

	UserWalletDTO getUserWallet (long id);
	
	FacilityWalletDTO getFacilityWallet(long id);
	
	List<WalletTransactionDTO> getUserTransactions(long userId);
	
	List<WalletTransactionDTO> getFacilityTransactions(long facilityId);
	
	void debitUserWallet(long userId, BigDecimal amount, String description);
	
	UserWalletDTO creditUserWallet(long userId, BigDecimal amount, String description);
	
	void creditFacilityWallet(long facilityId, BigDecimal amount, String description);
	
	void debitFacilityWallet(long facilityId, BigDecimal amount, String description);

	boolean hasEnoughBalance(Long id, BigDecimal amount);

	
}
