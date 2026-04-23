package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CourtDTO;
import com.example.demo.model.ResponseAPI;
import com.example.demo.service.CourtService;

@RestController
@RequestMapping("/courts")
public class CourtController {

	@Autowired
	@Qualifier("courtService")
	private CourtService courtService;

	@GetMapping
	public ResponseEntity<?> getAllCourts() {
		List<CourtDTO> courts = courtService.listAllCourts();
		return ResponseEntity.ok(new ResponseAPI<>(true, courts, "Courts retrieved successfully"));
	}

	@GetMapping("/facility/{id}")
	public ResponseEntity<?> getCourtsByFacility(@PathVariable int id) {
		List<CourtDTO> courts = courtService.listCourtsByFacilityId(id);
		return ResponseEntity.ok(new ResponseAPI<>(true, courts, "Courts retrieved successfully"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCourtById(@PathVariable int id) {
		try {
			CourtDTO court = courtService.getCourtById(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, court, "Court retrieved successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@PostMapping
	public ResponseEntity<?> addCourt(@RequestBody CourtDTO courtDTO) {
		try {
			CourtDTO newCourt = courtService.addCourt(courtDTO);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ResponseAPI<>(true, newCourt, "Court created successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateCourt(@PathVariable int id, @RequestBody CourtDTO courtDTO) {
		try {
			CourtDTO updatedCourt = courtService.updateCourt(id, courtDTO);
			return ResponseEntity.ok(new ResponseAPI<>(true, updatedCourt, "Court updated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCourt(@PathVariable int id) {
		try {
			courtService.deleteCourt(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, null, "Court deleted successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@PutMapping("/{id}/activate")
	public ResponseEntity<?> activateCourt(@PathVariable int id) {
		try {
			courtService.activateCourt(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, null, "Court activated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@PutMapping("/{id}/deactivate")
	public ResponseEntity<?> deactivateCourt(@PathVariable int id) {
		try {
			courtService.deactivateCourt(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, null, "Court deactivated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

}
