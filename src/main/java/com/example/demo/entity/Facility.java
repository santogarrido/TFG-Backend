package com.example.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "facilities")
public class Facility {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private String openTime;

	private String closeTime;

	private String location;
	
	private int latitude;
	
	private int longitude;
	
	private String imageUrl;

	private boolean activated;

	private boolean deleted;

	@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = false)
	@JsonIgnore
	private List<Court> courts;
	
	@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = false)
	@JsonIgnore
	private List<Wallet> wallets;

}
