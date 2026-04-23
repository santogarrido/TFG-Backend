package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.model.ResponseAPI;
import com.example.demo.model.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
		try {

			String username = request.get("username");
			String password = request.get("password");
			String token = authService.login(username, password);

			UserDTO userDTO = userService.getUserByUsername(username);

			Map<String, Object> data = Map.of("id", userDTO.getId(), "username", userDTO.getUsername(), "role", userDTO.getRole(), "token", token);

			return ResponseEntity.ok(new ResponseAPI<>(true, data, "User login successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseAPI<>(false, null, e.getMessage()));
		}

	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			User newUser = authService.register(user);
			Map<String, Object> data = Map.of("id", newUser.getId(), "username", newUser.getUsername(), "email",
					newUser.getEmail(), "name", newUser.getName(), "secondName", newUser.getSecondName());
			return ResponseEntity.ok(new ResponseAPI<>(true, data, "User registered successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

}
