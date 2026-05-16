package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.model.ResponseAPI;
import com.example.demo.model.UserDTO;
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

			return ResponseEntity.ok(new ResponseAPI<>(true, data, "Inicio de sesión correcto."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseAPI<>(false, null, e.getMessage()));
		}

	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			User newUser = authService.register(user);
			Map<String, Object> data = Map.of("id", newUser.getId(), "username", newUser.getUsername(), "email",
					newUser.getEmail(), "name", newUser.getName(), "secondName", newUser.getSecondName(), "activated", newUser.isActivated());
			return ResponseEntity.ok(new ResponseAPI<>(true, data, "Usuario registrado correctamente. Debes verificar tu correo para activar la cuenta e iniciar sesion."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

	@GetMapping("/activate")
	public ResponseEntity<String> activate(@RequestParam("token") String token) {
		try {
			authService.activateUserByToken(token);
			String html = """
					<!DOCTYPE html>
					<html lang="es">
					<head>
					  <meta charset="UTF-8">
					  <meta name="viewport" content="width=device-width, initial-scale=1.0">
					  <title>Cuenta activada</title>
					  <style>
					    body {
					      margin: 0;
					      min-height: 100vh;
					      display: grid;
					      place-items: center;
					      font-family: Arial, sans-serif;
					      background: #f7f7f7;
					      color: #1a1a1a;
					    }
					    .card {
					      background: #ffffff;
					      border: 1px solid #e5e5e5;
					      border-radius: 10px;
					      padding: 32px 24px;
					      text-align: center;
					      box-shadow: 0 8px 24px rgba(0,0,0,0.08);
					    }
					  </style>
					</head>
					<body>
					  <div class="card">Cuenta activada correctamente</div>
					</body>
					</html>
					""";
			return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
		} catch (RuntimeException e) {
			String html = """
					<!DOCTYPE html>
					<html lang="es">
					<head>
					  <meta charset="UTF-8">
					  <meta name="viewport" content="width=device-width, initial-scale=1.0">
					  <title>Error de activacion</title>
					  <style>
					    body {
					      margin: 0;
					      min-height: 100vh;
					      display: grid;
					      place-items: center;
					      font-family: Arial, sans-serif;
					      background: #f7f7f7;
					      color: #1a1a1a;
					    }
					    .card {
					      background: #ffffff;
					      border: 1px solid #e5e5e5;
					      border-radius: 10px;
					      padding: 32px 24px;
					      text-align: center;
					      box-shadow: 0 8px 24px rgba(0,0,0,0.08);
					    }
					  </style>
					</head>
					<body>
					  <div class="card">No se pudo activar la cuenta: %s</div>
					</body>
					</html>
					""".formatted(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_HTML).body(html);
		}
	}

	@PostMapping("/resend-activation")
	public ResponseEntity<?> resendActivation(@RequestBody Map<String, String> request) {
		try {
			String email = request.get("email");
			authService.resendActivationEmail(email);
			return ResponseEntity.ok(new ResponseAPI<>(true, null, "Email de activacion reenviado correctamente."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAPI<>(false, null, e.getMessage()));
		}
	}

}
