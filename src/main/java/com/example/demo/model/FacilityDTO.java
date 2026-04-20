package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {

	private int id;

	private String name;

	private String openTime;

	private String closeTime;

	private String location;

	private int latitude;
	
	private int longitude;
	
	private String imageUrl;

	private boolean activated;
	
	private boolean deleted;

	public FacilityDTO(String name, String openTime, String closeTime, String location, int latitude, int longitude, String imageUrl) {
		super();
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
	}

}
