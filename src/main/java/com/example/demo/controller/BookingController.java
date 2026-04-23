package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Booking;
import com.example.demo.model.BookingDTO;
import com.example.demo.model.ResponseAPI;
import com.example.demo.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	@Qualifier("bookingService")
	private BookingService bookingService;

	@GetMapping
	public ResponseEntity<?> getAllBookings() {
		List<BookingDTO> bookings = bookingService.getAllBookings();

		return ResponseEntity.ok(new ResponseAPI<>(true, bookings, "Bookings retrieved successfully"));

	}

	@PostMapping("/getAllBookings/{id}")
	public ResponseEntity<?> getAllBookings(@PathVariable long id) {

		List<BookingDTO> bookings = bookingService.getAllBookingsByFacility(id);

		return ResponseEntity.ok(new ResponseAPI<>(true, bookings, "Bookings retrieved succesfully"));

	}

	@GetMapping("/getByCourt/{courtId}")
	public ResponseEntity<?> getBookingsByCourt(@PathVariable int courtId) {
		List<BookingDTO> bookings = bookingService.getAllBookingsByCourt(courtId);

		return ResponseEntity.ok(new ResponseAPI<>(true, bookings, "Bookings retrieved successfully"));
	}

	@PostMapping("/getBooking/{id}")
	public ResponseEntity<?> getBookingById(@PathVariable long id) {
		try {
			BookingDTO booking = bookingService.getBookingById(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, booking, "Booking retrieved succesfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@PostMapping("/getBookingsByUser/{id}")
	public ResponseEntity<?> getBookingsByUser(@PathVariable long id) {

		List<BookingDTO> bookings = bookingService.getBookingByUser(id);

		if (bookings.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseAPI<>(false, null, "There are no bookings"));
		} else {
			return ResponseEntity.ok(new ResponseAPI<>(true, bookings, "Bookings retrieved succesfully"));
		}
	}

	@PostMapping("/addBooking")
	public ResponseEntity<?> addBooking(@RequestBody BookingDTO bookingDTO) {

		bookingService.addBooking(bookingDTO);
		return ResponseEntity.ok(new ResponseAPI<>(true, bookingDTO, "Booking added succesfully"));

	}

	@DeleteMapping("/cancelBooking/{id}")
	public ResponseEntity<?> deleteBooking(@PathVariable long id) {
		try {
			bookingService.cancelBooking(id);
			return ResponseEntity
					.ok(new ResponseAPI<>(true, bookingService.getBookingById(id), "Booking retrieved succesfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

}
