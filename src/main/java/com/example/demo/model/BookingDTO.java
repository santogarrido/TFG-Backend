package com.example.demo.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

	private int id;

	private int userId;

	private int courtId;
	
	private LocalDateTime bookingDateTime;

	private LocalDateTime courtDateTimeBooking;

	private int courtPrice;

	private boolean deleted;

	public BookingDTO(int userId, int courtId, LocalDateTime bookingDateTime, LocalDateTime courtDateTimeBooking, int courtPrice) {
		super();
		this.userId = userId;
		this.courtId = courtId;
		this.bookingDateTime = bookingDateTime;
		this.courtDateTimeBooking = courtDateTimeBooking;
		this.courtPrice = courtPrice;
	}
	
	

}
