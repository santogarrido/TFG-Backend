package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.WalletTransaction;

@Repository("walletTransactionRepository")
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Serializable> {

	List<WalletTransaction> findByWalletUserIdOrderByCreatedAtDesc(Long userId);

	List<WalletTransaction> findByWalletFacilityIdOrderByCreatedAtDesc(Long facilityId);
}
