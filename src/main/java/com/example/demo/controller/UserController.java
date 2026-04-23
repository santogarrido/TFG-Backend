package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ResponseAPI;
import com.example.demo.model.UserDTO;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		List<UserDTO> users = userService.listAllUsers();
		return ResponseEntity.ok(new ResponseAPI<>(true, users, "Users retrieved successfully."));

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable int id) {
		try {
			UserDTO user = userService.getUserById(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, user, "User retrieved successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		try {
			userService.deleteUser(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, null, "User deleted successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@PutMapping("/{id}/activate")
	public ResponseEntity<?> activateUser(@PathVariable int id) {
		try {
			userService.activateUser(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, null, "User activated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@PutMapping("/{id}/deactivate")
	public ResponseEntity<?> deactivateUser(@PathVariable int id) {
		try {
			userService.deactivateUser(id);
			return ResponseEntity.ok(new ResponseAPI<>(true, null, "User deactivated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
		try {
			UserDTO user = userService.updateUser(id, userDTO);

			return ResponseEntity.ok(new ResponseAPI<>(true, user, "User updated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAPI<>(false, null, e.getMessage()));
		}

	}

}
