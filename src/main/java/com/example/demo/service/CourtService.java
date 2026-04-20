package com.example.demo.service;

import java.util.List;

import com.example.demo.model.CourtDTO;

public interface CourtService {

	List<CourtDTO> listAllCourts();

	List<CourtDTO> listCourtsByFacilityId(int id);

	CourtDTO getCourtById(int id);

	CourtDTO addCourt(CourtDTO courtDTO);

	CourtDTO updateCourt(int id, CourtDTO courtDTO);

	void deleteCourt(int id);
	
	void activateCourt(int id);
	
	void deactivateCourt(int id);

}