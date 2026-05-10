package com.example.demo.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtDTO {

	private int id;

	private String name;

	private String category;

	private int bookingDuration;

	private BigDecimal courtPrice;

	private boolean activated;

	private boolean deleted;

	private int facilityId;

	public CourtDTO(String name, String category, int bookingDuration, int facilityId, BigDecimal courtPrice) {
		super();
		this.name = name;
		this.category = category;
		this.bookingDuration = bookingDuration;
		this.facilityId = facilityId;
		this.courtPrice = courtPrice;
	}
	
	
	
	

//	private Facility facility;

//	private List<Booking> bookings;

}
