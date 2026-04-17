package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Booking;

@Repository("bookingRepository")
public interface BookingRepository extends JpaRepository<Booking, Serializable> {

	List<Booking> findByCourtIdAndDeletedFalse(Long courtId);

}