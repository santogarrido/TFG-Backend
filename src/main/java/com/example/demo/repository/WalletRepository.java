package com.example.demo.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Wallet;

@Repository("walletRepository")
public interface WalletRepository extends JpaRepository<Wallet, Serializable> {

	Optional<Wallet> findByUserId(Long userId);
	
	Optional<Wallet> findByFacilityId(Long facilityId);
	
}
