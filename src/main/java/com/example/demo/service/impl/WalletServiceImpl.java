package com.example.demo.service.impl;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Wallet;
import com.example.demo.model.BookingDTO;
import com.example.demo.model.WalletDTO;
import com.example.demo.repository.WalletRepository;
import com.example.demo.service.WalletService;

@Service("walletService")
public class WalletServiceImpl implements WalletService {

	@Autowired
	@Qualifier("walletRepository")
	private WalletRepository walletRepository;

	@Override
	public WalletDTO getUserWallet(long id) {
		Wallet wallet = walletRepository.findByUserId(id)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		return transform(wallet);
	}

	@Override
	public WalletDTO getFacilityWallet(long id) {
		Wallet wallet = walletRepository.findByFacilityId(id)
				.orElseThrow(() -> new RuntimeException("Wallet not found for facility"));
		return transform(wallet);
	}

	@Override
	@Transactional
	public void debitUserWallet(long userId, BigDecimal amount) {
		validateAmount(amount);
		Wallet wallet = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));

		if (wallet.getMoney().compareTo(amount) < 0) {
			throw new RuntimeException("Insufficient balance");
		}

		wallet.setMoney(wallet.getMoney().subtract(amount));
		walletRepository.save(wallet);
	}
	
	@Override
	@Transactional
	public void creditUserWallet(long userId, BigDecimal amount) {
		validateAmount(amount);
		Wallet wallet = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		
		wallet.setMoney(wallet.getMoney().add(amount));
		walletRepository.save(wallet);
	}

	@Override
	@Transactional
	public void creditFacilityWallet(long facilityId, BigDecimal amount) {
		validateAmount(amount);
		Wallet wallet = walletRepository.findByFacilityId(facilityId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for facility"));

		wallet.setMoney(wallet.getMoney().add(amount));
		walletRepository.save(wallet);
	}
	
	@Override
	@Transactional
	public void debitFacilityWallet(long facilityId, BigDecimal amount) {
		validateAmount(amount);
		Wallet wallet = walletRepository.findByFacilityId(facilityId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for facility"));
		
		if (wallet.getMoney().compareTo(amount) < 0) {
			throw new RuntimeException("Facility has insufficient balance for refund");
		}
		
		wallet.setMoney(wallet.getMoney().subtract(amount));
		walletRepository.save(wallet);
	}

	private void validateAmount(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("Amount must be greater than zero");
		}
	}

	private WalletDTO transform(Wallet wallet) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(wallet, WalletDTO.class);
	}
}
