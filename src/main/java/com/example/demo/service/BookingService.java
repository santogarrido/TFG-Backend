package com.example.demo.service;

import java.util.List;

import com.example.demo.model.BookingDTO;

public interface BookingService {

	List<BookingDTO> getAllBookings();

	List<BookingDTO> getAllBookingsByFacility(long id);

	List<BookingDTO> getAllBookingsByCourt(long id);

	BookingDTO getBookingById(long id);

	List<BookingDTO> getBookingByUser(long id);

	void addBooking(BookingDTO bookingDTO);

	void cancelBooking(long id);

}