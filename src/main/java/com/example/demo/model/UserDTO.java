package com.example.demo.model;

import java.util.List;

import com.example.demo.entity.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private int id;

	private String name;

	private String secondName;

	private String email;

	private String username;

	private String role;

	private boolean activated;

	private boolean deleted;

	public UserDTO(String name, String secondName, String email, String username) {
		super();
		this.name = name;
		this.secondName = secondName;
		this.email = email;
		this.username = username;
	}
}
