package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.WalletReferenceType;
import com.example.demo.entity.Wallet;
import com.example.demo.entity.WalletTransaction;
import com.example.demo.model.WalletDTO;
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
	public List<WalletTransactionDTO> getUserTransactions(long userId) {
		List<WalletTransactionDTO> transactionDTOs = new ArrayList<>();
		for (WalletTransaction transaction : walletTransactionRepository
				.findByWalletUserId(userId)) {
			transactionDTOs.add(transformTransaction(transaction));
		}
		return transactionDTOs;
	}

	@Override
	public List<WalletTransactionDTO> getFacilityTransactions(long facilityId) {
		List<WalletTransactionDTO> transactionDTOs = new ArrayList<>();
		for (WalletTransaction transaction : walletTransactionRepository
				.findByWalletFacilityId(facilityId)) {
			transactionDTOs.add(transformTransaction(transaction));
		}
		return transactionDTOs;
	}

	@Override
	@Transactional
	public void debitUserWallet(long userId, BigDecimal amount) {
		Wallet wallet = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		applyTransaction(wallet, true, WalletReferenceType.MANUAL, null, amount, "Manual debit from user wallet");
	}

	@Override
	@Transactional
	public void creditUserWallet(long userId, BigDecimal amount) {
		Wallet wallet = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		applyTransaction(wallet, false, WalletReferenceType.MANUAL, null, amount, "Manual credit to user wallet");
	}

	@Override
	@Transactional
	public void creditFacilityWallet(long facilityId, BigDecimal amount) {
		Wallet wallet = walletRepository.findByFacilityId(facilityId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for facility"));
		applyTransaction(wallet, false, WalletReferenceType.MANUAL, null, amount, "Manual credit to facility wallet");
	}

	@Override
	@Transactional
	public void debitFacilityWallet(long facilityId, BigDecimal amount) {
		Wallet wallet = walletRepository.findByFacilityId(facilityId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for facility"));
		applyTransaction(wallet, true, WalletReferenceType.MANUAL, null, amount, "Manual debit from facility wallet");
	}

	@Override
	@Transactional
	public void debitUserWalletForBooking(long userId, BigDecimal amount, long bookingId) {
		Wallet wallet = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		applyTransaction(wallet, true, WalletReferenceType.BOOKING, bookingId, amount, "Booking payment");
	}

	@Override
	@Transactional
	public void creditFacilityWalletForBooking(long facilityId, BigDecimal amount, long bookingId) {
		Wallet wallet = walletRepository.findByFacilityId(facilityId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for facility"));
		applyTransaction(wallet, false, WalletReferenceType.BOOKING, bookingId, amount, "Booking income");
	}

	@Override
	@Transactional
	public void debitFacilityWalletForRefund(long facilityId, BigDecimal amount, long bookingId) {
		Wallet wallet = walletRepository.findByFacilityId(facilityId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for facility"));
		applyTransaction(wallet, true, WalletReferenceType.BOOKING_REFUND, bookingId, amount, "Booking refund");
	}

	@Override
	@Transactional
	public void creditUserWalletForRefund(long userId, BigDecimal amount, long bookingId) {
		Wallet wallet = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Wallet not found for user"));
		applyTransaction(wallet, false, WalletReferenceType.BOOKING_REFUND, bookingId, amount, "Booking refund");
	}

	private void applyTransaction(Wallet wallet, boolean isDebit, WalletReferenceType referenceType, Long referenceId,
			BigDecimal amount, String description) {
		BigDecimal balanceBefore = wallet.getMoney();
		if (balanceBefore == null) {
			balanceBefore = BigDecimal.ZERO;
		}

		BigDecimal balanceAfter;
		if (isDebit) {
			if (balanceBefore.compareTo(amount) < 0) {
				throw new RuntimeException("Insufficient balance");
			}
			balanceAfter = balanceBefore.subtract(amount);
		} else {
			balanceAfter = balanceBefore.add(amount);
		}

		wallet.setMoney(balanceAfter);
		walletRepository.save(wallet);

		WalletTransaction transaction = new WalletTransaction();
		transaction.setWallet(wallet);
		transaction.setReferenceType(referenceType);
		transaction.setReferenceId(referenceId);
		transaction.setAmount(amount);
		transaction.setBalanceBefore(balanceBefore);
		transaction.setBalanceAfter(balanceAfter);
		transaction.setDescription(description);
		walletTransactionRepository.save(transaction);
	}

	private WalletDTO transform(Wallet wallet) {
		ModelMapper modelMapper = new ModelMapper();
		WalletDTO walletDTO = modelMapper.map(wallet, WalletDTO.class);
		walletDTO.setUserId(wallet.getUser() != null ? wallet.getUser().getId().intValue() : 0);
		walletDTO.setFacilityId(wallet.getFacility() != null ? wallet.getFacility().getId().intValue() : 0);
		return walletDTO;
	}

	private WalletTransactionDTO transformTransaction(WalletTransaction transaction) {
		ModelMapper modelMapper = new ModelMapper();
		WalletTransactionDTO transactionDTO = modelMapper.map(transaction, WalletTransactionDTO.class);
		transactionDTO.setWalletId(transaction.getWallet().getId());
		transactionDTO.setUserId(
				transaction.getWallet().getUser() != null ? transaction.getWallet().getUser().getId().intValue() : 0);
		transactionDTO.setFacilityId(
				transaction.getWallet().getFacility() != null ? transaction.getWallet().getFacility().getId().intValue()
						: 0);
		return transactionDTO;
	}
}
