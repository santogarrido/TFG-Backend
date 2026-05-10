package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Wallet;
import com.example.demo.entity.WalletTransaction;
import com.example.demo.model.FacilityWalletDTO;
import com.example.demo.model.UserWalletDTO;
import com.example.demo.model.WalletTransactionDTO;
import com.example.demo.repository.WalletRepository;
import com.example.demo.repository.WalletTransactionRepository;
import com.example.demo.service.WalletService;

@Service("walletService")
public class WalletServiceImpl implements WalletService {

	@Autowired
	@Qualifier("walletRepository")
	private WalletRepository walletRepository;

	@Autowired
	@Qualifier("walletTransactionRepository")
	private WalletTransactionRepository walletTransactionRepository;

	@Override
	public UserWalletDTO getUserWallet(long id) {
		Wallet wallet = walletRepository.findByUserId(id)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		
		UserWalletDTO dto = new UserWalletDTO();
		dto.setId(wallet.getId().intValue());
		dto.setAmount(wallet.getAmount());
		dto.setUserId(wallet.getUser().getId().intValue());
		
		return dto;
		
	}

	@Override
	public FacilityWalletDTO getFacilityWallet(long id) {
		Wallet wallet = walletRepository.findByFacilityId(id)
				.orElseThrow(() -> new RuntimeException("Wallet not found for facility"));

		FacilityWalletDTO dto = new FacilityWalletDTO();
		dto.setId(wallet.getId().intValue());
		dto.setAmount(wallet.getAmount());
		dto.setFacilityId(wallet.getFacility().getId().intValue());
		
		return dto;
	}

	@Override
	public List<WalletTransactionDTO> getUserTransactions(long userId) {
		List<WalletTransactionDTO> transactionDTOs = new ArrayList<>();
		for (WalletTransaction transaction : walletTransactionRepository.findByWalletUserId(userId)) {
			transactionDTOs.add(transformTransaction(transaction));
		}
		return transactionDTOs;
	}

	@Override
	public List<WalletTransactionDTO> getFacilityTransactions(long facilityId) {
		List<WalletTransactionDTO> transactionDTOs = new ArrayList<>();
		for (WalletTransaction transaction : walletTransactionRepository.findByWalletFacilityId(facilityId)) {
			transactionDTOs.add(transformTransaction(transaction));
		}
		return transactionDTOs;
	}

	
	@Override
	@Transactional
	public void debitUserWallet(long userId, BigDecimal amount, String description) {
	    Wallet wallet = walletRepository.findByUserId(userId)
	            .orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		
	    applyTransaction(wallet, true, amount, description);
	}

	@Override
	@Transactional
	public UserWalletDTO creditUserWallet(long userId, BigDecimal amount, String description) {
	    Wallet wallet = walletRepository.findByUserId(userId)
	            .orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		
	    applyTransaction(wallet, false, amount, description);
		
	    return transformUserWallet(wallet);
	}

	@Override
	@Transactional
	public void creditFacilityWallet(long facilityId, BigDecimal amount, String description) {
	    Wallet wallet = walletRepository.findByFacilityId(facilityId)
	            .orElseThrow(() -> new RuntimeException("Wallet not found for facility"));

	    applyTransaction(wallet, false, amount, description);
	}

	@Override
	@Transactional
	public void debitFacilityWallet(long facilityId, BigDecimal amount, String description) {
		Wallet wallet = walletRepository.findByFacilityId(facilityId)
	            .orElseThrow(() -> new RuntimeException("Wallet not found for facility"));

	    applyTransaction(wallet, true, amount, description);
		
	}
	
	private void applyTransaction(Wallet wallet, boolean isDebit, BigDecimal amount, String description) {
		BigDecimal balanceBefore = wallet.getAmount();
		
		if (balanceBefore == null) {
			balanceBefore = BigDecimal.ZERO;
		}

		BigDecimal balanceAfter;
		if (isDebit) {
			if (balanceBefore.compareTo(amount) < 0) {
				throw new RuntimeException("Insufficient money");
			}
			balanceAfter = balanceBefore.subtract(amount);
		} else {
			balanceAfter = balanceBefore.add(amount);
		}

		wallet.setAmount(balanceAfter);
		walletRepository.save(wallet);

		WalletTransaction transaction = new WalletTransaction();
		transaction.setWallet(wallet);
		transaction.setAmount(amount);
		transaction.setBalanceBefore(balanceBefore);
		transaction.setBalanceAfter(balanceAfter);
		transaction.setDescription(description);
		walletTransactionRepository.save(transaction);
	}

	
	private UserWalletDTO transformUserWallet(Wallet wallet) {
		
		UserWalletDTO dto = new UserWalletDTO();
		dto.setId(wallet.getId().intValue());
		dto.setAmount(wallet.getAmount());
		dto.setUserId(wallet.getUser().getId().intValue());
		
		return dto;
	}
	
	private WalletTransactionDTO transformTransaction(WalletTransaction transaction) {
		ModelMapper modelMapper = new ModelMapper();
		WalletTransactionDTO transactionDTO = modelMapper.map(transaction, WalletTransactionDTO.class);
		return transactionDTO;
	}
	
	@Override
	public boolean hasEnoughBalance(Long id, BigDecimal amount) {
		Wallet wallet = walletRepository.findByUserId(id)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));

		BigDecimal balance = wallet.getAmount() != null ? wallet.getAmount() : BigDecimal.ZERO;

		return balance.compareTo(amount) >= 0;
	}


}
