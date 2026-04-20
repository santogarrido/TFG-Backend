package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Facility;
import com.example.demo.entity.Wallet;
import com.example.demo.model.FacilityDTO;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.repository.WalletRepository;
import com.example.demo.service.FacilityService;

@Service("facilityService")
public class FacilityServiceImpl implements FacilityService {

	@Autowired
	@Qualifier("facilityRepository")
	private FacilityRepository facilityRepository;
	
	@Autowired
	@Qualifier("walletRepository")
	private WalletRepository walletRepository;

	/**
	 * Returns all the facilities
	 */
	@Override
	public List<FacilityDTO> listAllFacilities() {
		List<FacilityDTO> facilities = new ArrayList<>();
		for (Facility f : facilityRepository.findAll())
			if(f.isDeleted() == false)
				facilities.add(transform(f));
		return facilities;
	}

	/**
	 * Returns the facility by id
	 */
	@Override
	public FacilityDTO getFacilityById(long id) {
		FacilityDTO facilityDTO = transform(
				facilityRepository.findById(id).orElseThrow(() -> new RuntimeException("Facility not found")));
		return facilityDTO;
	}

	/**
	 * Creates a facility and returns its id
	 */
	@Override
	@Transactional
	public void addFacility(FacilityDTO facilityDTO) {

		Facility facility = new Facility();
		
		facility.setName(facilityDTO.getName());
		facility.setCloseTime(facilityDTO.getCloseTime());
		facility.setOpenTime(facilityDTO.getOpenTime());
		facility.setLocation(facilityDTO.getLocation());
		facility.setLatitude(facilityDTO.getLatitude());
		facility.setLongitude(facilityDTO.getLongitude());
		facility.setActivated(true);
		facility.setDeleted(false);
		Facility savedFacility = facilityRepository.save(facility);
		
		Wallet wallet = new Wallet();
		wallet.setFacility(savedFacility);
		wallet.setMoney(BigDecimal.ZERO);
		walletRepository.save(wallet);

	}

	/**
	 * Soft delete a facility
	 */
	@Override
	public void deleteFacility(long id) {
		Facility facility = facilityRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Facility not found"));
		facility.setDeleted(true);
		facilityRepository.save(facility);

	}

	/**
	 * Update a facility and returns the facility
	 */
	@Override
	public FacilityDTO updateFacility(long id, FacilityDTO facilityDTO) {
		Facility facility = facilityRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Facility not found"));

		facility.setName(facilityDTO.getName());
		facility.setLocation(facilityDTO.getLocation());
		facility.setOpenTime(facilityDTO.getOpenTime());
		facility.setCloseTime(facilityDTO.getCloseTime());

		return transform(facilityRepository.save(facility));
	}

	/**
	 * Turns the activate attribute to true
	 */
	@Override
	public void activateFacility(long id) {
		Facility facility = facilityRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Facility not found"));
		facility.setActivated(true);
		facilityRepository.save(facility);

	}

	/**
	 * Turns the activate attribute to false
	 */
	@Override
	public void deactivateFacility(long id) {
		Facility facility = facilityRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Facility not found"));
		facility.setActivated(false);
		facilityRepository.save(facility);

	}

	// Transform entity into model
	private FacilityDTO transform(Facility facility) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(facility, FacilityDTO.class);
	}

	// Transform model into entity
	private Facility transform(FacilityDTO facilityDTO) {

		ModelMapper modelMapper = new ModelMapper();
		Facility facility = modelMapper.map(facilityDTO, Facility.class);

		return facility;

	}

}
