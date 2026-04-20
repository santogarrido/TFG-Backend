package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.model.WalletDTO;
import com.example.demo.model.WalletTransactionDTO;

public interface WalletService {

	WalletDTO getUserWallet (long id);
	
	WalletDTO getFacilityWallet(long id);
	
	List<WalletTransactionDTO> getUserTransactions(long userId);
	
	List<WalletTransactionDTO> getFacilityTransactions(long facilityId);
	
	void debitUserWallet(long userId, BigDecimal amount);
	
	void creditUserWallet(long userId, BigDecimal amount);
	
	void creditFacilityWallet(long facilityId, BigDecimal amount);
	
	void debitFacilityWallet(long facilityId, BigDecimal amount);
	
	void debitUserWalletForBooking(long userId, BigDecimal amount, long bookingId);
	
	void creditFacilityWalletForBooking(long facilityId, BigDecimal amount, long bookingId);
	
	void debitFacilityWalletForRefund(long facilityId, BigDecimal amount, long bookingId);
	
	void creditUserWalletForRefund(long userId, BigDecimal amount, long bookingId);
	
}
