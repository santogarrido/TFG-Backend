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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.FacilityDTO;
import com.example.demo.model.FacilityWalletDTO;
import com.example.demo.model.ResponseAPI;
import com.example.demo.service.FacilityService;
import com.example.demo.service.MinIoService;
import com.example.demo.service.WalletService;

@RestController
@RequestMapping("/facilities")
public class FacilityController {

	@Autowired
	@Qualifier("facilityService")
	private FacilityService facilityService;
	
	@Autowired
	@Qualifier("walletService")
	private WalletService walletService;

	@Autowired
	@Qualifier("minIoService")
	private MinIoService minIoService;
	
	@GetMapping
	public ResponseEntity<?> getAllFacilities(){
		List<FacilityDTO> facilities = facilityService.listAllFacilities();
		return ResponseEntity.ok(new ResponseAPI<>(true, facilities, "Facilities retrieved succesfully"));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getFacilityById(@PathVariable int id){
		try {
			FacilityDTO facilityDTO = facilityService.getFacilityById(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, facilityDTO, "Facility retrieved successfully"));
		}catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	@PostMapping(consumes = {"multipart/form-data"})
	public ResponseEntity<?> addFacility(@RequestPart("facility") FacilityDTO facilityDTO,
        @RequestPart("file") MultipartFile file){
		
		try {

			// Validar que sea imagen
			if (file != null && !file.isEmpty()) {
				if (!file.getContentType().startsWith("image/")) {
					return ResponseEntity.badRequest()
							.body(new ResponseAPI<>(false, null, "Only image files are allowed"));
				}

				// Subir a MinIO
				String imageUrl = minIoService.uploadImage(file);

				// Guardar URL en el DTO
				facilityDTO.setImageUrl(imageUrl);
			}

			// Guardar facility en DB
			FacilityDTO savedFacility = facilityService.addFacility(facilityDTO);

			return ResponseEntity.ok(
					new ResponseAPI<>(true, savedFacility, "Facility added successfully")
			);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteFaciliy(@PathVariable long id){
		
		try {
			facilityService.deleteFacility(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, facilityService.getFacilityById(id), "Facility added succesfully"));
		}catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateFacility(@PathVariable long id ,@RequestBody FacilityDTO facilityDTO){
		try {
			FacilityDTO facility = facilityService.updateFacility(id, facilityDTO);
			return ResponseEntity.ok(new ResponseAPI<>(true, facility, "Facility added successfully"));
		}catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	
	@PostMapping("/{id}/activate")
	public ResponseEntity<?> activateFacility(@PathVariable long id){
		try {
			facilityService.activateFacility(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, facilityService.getFacilityById(id), "Facility activated successfully"));
		}catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	
	@PostMapping("/{id}/deactivate")
	public ResponseEntity<?> deactivateFacility(@PathVariable long id){
		try {
			facilityService.deactivateFacility(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, facilityService.getFacilityById(id), "Facility deactivated successfully"));
		}catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}
	
	// Wallet Implementation
	
	@GetMapping("/{id}/wallet")
	public ResponseEntity<?> getFacilityWallet(@PathVariable long id) {
	    try {
	        FacilityWalletDTO wallet = walletService.getFacilityWallet(id);
	        return ResponseEntity.ok(new ResponseAPI<>(true, wallet, "Facility wallet retrieved successfully"));
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ResponseAPI<>(false, null, e.getMessage()));
	    }
	}
	
	
	
}
