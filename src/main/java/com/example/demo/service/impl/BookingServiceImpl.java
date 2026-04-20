package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Court;
import com.example.demo.entity.User;
import com.example.demo.model.BookingDTO;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.CourtRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.WalletService;

@Service("bookingService")
public class BookingServiceImpl implements BookingService {

	@Autowired
	@Qualifier("bookingRepository")
	private BookingRepository bookingRepository;

	@Autowired
	@Qualifier("courtRepository")
	private CourtRepository courtRepository;

	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;
	
	@Autowired
	@Qualifier("walletService")
	private WalletService walletService;

	@Override
	public List<BookingDTO> getAllBookings() {
		List<Booking> bookings = bookingRepository.findAll();
		List<BookingDTO> bookingDTOs = new ArrayList<>();
		for (Booking b : bookings) {
			bookingDTOs.add(transform(b));
		}
		return bookingDTOs;
	}

	@Override
	public List<BookingDTO> getAllBookingsByFacility(long id) {
		List<BookingDTO> bookings = new ArrayList<>();
		for (Booking b : bookingRepository.findAll()) {
			if (b.getCourt().getFacility().getId() == id) {
				bookings.add(transform(b));
			}
		}
		return bookings;
	}

	@Override
	public List<BookingDTO> getAllBookingsByCourt(long id) {
		List<Booking> bookings = bookingRepository.findByCourtIdAndDeletedFalse(id);
		List<BookingDTO> bookingDTOs = new ArrayList<>();
		for (Booking b : bookings) {
			bookingDTOs.add(transform(b));
		}
		return bookingDTOs;
	}

	@Override
	public BookingDTO getBookingById(long id) {
		BookingDTO bookingDTO = transform(
				bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found")));

		return bookingDTO;
	}

	@Override
	public List<BookingDTO> getBookingByUser(long id) {

		List<BookingDTO> bookingsByUser = new ArrayList<>();
		for (Booking b : bookingRepository.findAll()) {
			if (b.getUser().getId() == id) {
				bookingsByUser.add(transform(b));
			}
		}

		return bookingsByUser;
	}

	@Override
	@Transactional
	public void addBooking(BookingDTO bookingDTO) {

		Booking booking = new Booking();
		booking.setCourtDateTimeBooking(bookingDTO.getCourtDateTimeBooking());
		booking.setBookingDateTime(bookingDTO.getBookingDateTime());
		booking.setDeleted(false);

		Court court = courtRepository.findById(bookingDTO.getCourtId())
				.orElseThrow(() -> new RuntimeException("Court not found"));
		User user = userRepository.findById(bookingDTO.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		booking.setCourt(court);
		booking.setUser(user);

		bookingRepository.save(booking);
		
		BigDecimal amount = BigDecimal.valueOf(court.getCourtPrice());
		walletService.debitUserWallet(user.getId(), amount);
		walletService.creditFacilityWallet(court.getFacility().getId(), amount);

	}

	@Override
	@Transactional
	public void cancelBooking(long id) {

		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
		if (booking.isDeleted()) {
			throw new RuntimeException("Booking already cancelled");
		}
		
		BigDecimal amount = BigDecimal.valueOf(booking.getCourt().getCourtPrice());
		walletService.debitFacilityWallet(booking.getCourt().getFacility().getId(), amount);
		walletService.creditUserWallet(booking.getUser().getId(), amount);
		
		booking.setDeleted(true);
		bookingRepository.save(booking);

	}

	// Transform entity into model
	private BookingDTO transform(Booking booking) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(booking, BookingDTO.class);
	}

	// Transform model into entity
	private Booking transform(BookingDTO bookingDTO) {

		ModelMapper modelMapper = new ModelMapper();
		Booking booking = modelMapper.map(bookingDTO, Booking.class);

		return booking;

	}

}
