package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Court;
import com.example.demo.model.CourtDTO;
import com.example.demo.repository.CourtRepository;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.service.CourtService;
import com.example.demo.service.FacilityService;

@Service("courtService")
public class CourtServiceImpl implements CourtService {

	@Autowired
	@Qualifier("courtRepository")
	private CourtRepository courtRepository;

	@Autowired
	@Qualifier("facilityRepository")
	private FacilityRepository facilityRepository;

	/**
	 * Get all Courts in database
	 */
	@Override
	public List<CourtDTO> listAllCourts() {
		List<CourtDTO> courtsDTO = new ArrayList<>();
		for (Court court : courtRepository.findAll()) {
			courtsDTO.add(transform(court));
		}
		return courtsDTO;
	}

	/**
	 * Get all courts existing in a Facility
	 */
	@Override
	public List<CourtDTO> listCourtsByFacilityId(int id) {
		List<CourtDTO> courtsDTO = new ArrayList<>();
		for (Court court : courtRepository.findByFacilityId(id)) {
			if(court.isDeleted() == false)
				courtsDTO.add(transform(court));
		}
		return courtsDTO;
	}

	/**
	 * Get Court by Id
	 */
	@Override
	public CourtDTO getCourtById(int id) {
		CourtDTO courtDTO = transform(
				courtRepository.findById(id).orElseThrow(() -> new RuntimeException("Court not found")));
		return courtDTO;
	}

	/**
	 * Create a new Court
	 */
	@Override
	public CourtDTO addCourt(CourtDTO courtDTO) {
//		Court court = transform(courtDTO);
		Court court = new Court();
		court.setName(courtDTO.getName());
		court.setCategory(courtDTO.getCategory());
		court.setBookingDuration(courtDTO.getBookingDuration());
		court.setActivated(true);
		court.setDeleted(false);
		court.setFacility(facilityRepository.findById(courtDTO.getFacilityId()).orElse(null));
		return transform(courtRepository.save(court));
	}

	/**
	 * Update an existing Court
	 */
	@Override
	public CourtDTO updateCourt(int id, CourtDTO courtDTO) {
		Court court = courtRepository.findById(id).orElseThrow(() -> new RuntimeException("Court not found"));
		court.setName(courtDTO.getName());
		court.setBookingDuration(courtDTO.getBookingDuration());

		return transform(courtRepository.save(court));
	}

	/**
	 * Soft delete a Court
	 */
	@Override
	public void deleteCourt(int id) {
		Court court = courtRepository.findById(id).orElseThrow(() -> new RuntimeException("Court not found"));
		court.setDeleted(true);
		court.setActivated(false);
		courtRepository.save(court);
	}

	/**
	 * Activate a Court
	 */
	@Override
	public void activateCourt(int id) {
		Court court = courtRepository.findById(id).orElseThrow(() -> new RuntimeException("Court not found"));
		court.setActivated(true);
		courtRepository.save(court);
	}

	/**
	 * Deactivate a Court
	 */
	@Override
	public void deactivateCourt(int id) {
		Court court = courtRepository.findById(id).orElseThrow(() -> new RuntimeException("Court not found"));
		court.setActivated(false);
		courtRepository.save(court);
	}

	/**
	 * Transform entity to model
	 * 
	 * @param court
	 * @return
	 */
	private CourtDTO transform(Court court) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(court, CourtDTO.class);
	}

	/**
	 * Transform model to entity
	 * 
	 * @param courtDTO
	 * @return
	 */

	private Court transform(CourtDTO courtDTO) {

		ModelMapper modelMapper = new ModelMapper();
		Court court = modelMapper.map(courtDTO, Court.class);

		return court;

	}

}
