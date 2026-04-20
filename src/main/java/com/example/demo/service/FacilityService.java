package com.example.demo.service;

import java.util.List;

import com.example.demo.model.FacilityDTO;

public interface FacilityService {

	List<FacilityDTO> listAllFacilities();

	FacilityDTO getFacilityById(long id);

	void addFacility(FacilityDTO facilityDTO);

	void deleteFacility(long id);

	FacilityDTO updateFacility(long id, FacilityDTO facilityDTO);

	void activateFacility(long id);

	void deactivateFacility(long id);

}