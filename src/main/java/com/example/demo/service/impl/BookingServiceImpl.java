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
				bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada.")));

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

		Court court = courtRepository.findById(bookingDTO.getCourtId())
				.orElseThrow(() -> new RuntimeException("Pista no encontrada."));

		User user = userRepository.findById(bookingDTO.getUserId())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

		BigDecimal amount = court.getCourtPrice();

		if (!walletService.hasEnoughBalance(user.getId(), amount)) {
			throw new RuntimeException("Saldo insuficiente.");
		}

		Booking booking = new Booking();
		booking.setCourtDateTimeBooking(bookingDTO.getCourtDateTimeBooking());
		booking.setBookingDateTime(bookingDTO.getBookingDateTime());
		booking.setDeleted(false);
		booking.setCourtPrice(court.getCourtPrice());
		booking.setCourt(court);
		booking.setUser(user);

		bookingRepository.save(booking);

		walletService.debitUserWallet(user.getId(), amount, "Resrerva de pista: " + court.getName());
		walletService.creditFacilityWallet(court.getFacility().getId(), amount, "Pista reservada por: " + user.getUsername());
	}

	@Override
	@Transactional
	public void cancelBooking(long id) {

		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada."));
		if (booking.isDeleted()) {
			throw new RuntimeException("La reserva ya está cancelada.");
		}

		BigDecimal amount = booking.getCourt().getCourtPrice();
		walletService.debitFacilityWallet(booking.getCourt().getFacility().getId(), amount, "Devolución de la reserva de pista: " + booking.getCourt().getName());
		walletService.creditUserWallet(booking.getUser().getId(), amount, "Devolución de la reserva de pista: " + booking.getCourt().getName());

		booking.setDeleted(true);
		bookingRepository.save(booking);

	}

	// Transform entity into model
	private BookingDTO transform(Booking booking) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(booking, BookingDTO.class);
	}

}
