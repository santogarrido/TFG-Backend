package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.FacilityDTO;
import com.example.demo.model.ResponseAPI;
import com.example.demo.service.FacilityService;

@RestController
@RequestMapping("/facilities")
public class FacilityController {

	@Autowired
	@Qualifier("facilityService")
	private FacilityService facilityService;
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllFacilities(){
		List<FacilityDTO> facilities = facilityService.listAllFacilities();
		return ResponseEntity.ok(new ResponseAPI<>(true, facilities, "Facilities retrieved succesfully"));
	}
	
	@GetMapping("/getFacility/{id}")
	public ResponseEntity<?> getFacilityById(@PathVariable int id){
		try {
			FacilityDTO facilityDTO = facilityService.getFacilityById(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, facilityDTO, "Facility retrieved successfully"));
		}catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	@PostMapping("/addFacility")
	public ResponseEntity<?> addFacility(
			@ModelAttribute FacilityDTO facilityDTO,
	        @RequestParam MultipartFile imagen){
		
		facilityService.addFacility(facilityDTO);
		return ResponseEntity.ok(new ResponseAPI<>(true, facilityDTO, "Facility added succesfully"));
		
	}
	
	@DeleteMapping("/deleteFacility/{id}")
	public ResponseEntity<?> deleteFaciliy(@PathVariable long id){
		
		try {
			facilityService.deleteFacility(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, facilityService.getFacilityById(id), "Facility added succesfully"));
		}catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	@PutMapping("/updateFacility/{id}")
	public ResponseEntity<?> updateFacility(@PathVariable long id ,@RequestBody FacilityDTO facilityDTO){
		try {
			FacilityDTO facility = facilityService.updateFacility(id, facilityDTO);
			return ResponseEntity.ok(new ResponseAPI<>(true, facility, "Facility added successfully"));
		}catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	
	@PostMapping("/activateFacility/{id}")
	public ResponseEntity<?> activateFacility(@PathVariable long id){
		try {
			facilityService.activateFacility(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, facilityService.getFacilityById(id), "Facility activated successfully"));
		}catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	
	@PostMapping("/deactivateFacility/{id}")
	public ResponseEntity<?> deactivateFacility(@PathVariable long id){
		try {
			facilityService.deactivateFacility(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, facilityService.getFacilityById(id), "Facility deactivated successfully"));
		}catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
}
