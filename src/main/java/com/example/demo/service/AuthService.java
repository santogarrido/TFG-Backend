package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.User;
import com.example.demo.entity.Wallet;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WalletRepository;
import com.example.demo.security.JwtTokenProvider;

@Service
public class AuthService {
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private EmailService emailService;

	@org.springframework.beans.factory.annotation.Value("${app.frontend.activation-url:http://localhost:8080/auth/activate?token=}")
	private String activationBaseUrl;

	public String login(String username, String password) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));

		if (user.isDeleted() || !user.isActivated()) {
			throw new RuntimeException("User is deleted or deactivated");
		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		return tokenProvider.generateToken(authentication);
	}

	@Transactional
	public User register(User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("El nombre de usuario ya existe!");
		}
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("El email ya existe!");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		user.setActivated(false);
		user.setDeleted(false);
		String activationToken = UUID.randomUUID().toString();
		user.setActivationToken(activationToken);
		user.setActivationTokenExpiresAt(LocalDateTime.now().plusHours(24));
		User savedUser = userRepository.save(user);

		Wallet wallet = new Wallet();
		wallet.setUser(savedUser);
		wallet.setAmount(BigDecimal.ZERO);
		walletRepository.save(wallet);

		try {
			String activationLink = activationBaseUrl + activationToken;
			emailService.sendActivationEmail(savedUser.getEmail(), savedUser.getUsername(), activationLink);
		} catch (Exception e) {
			logger.error("No se pudo enviar email de activacion a {}: {}", savedUser.getEmail(), e.getMessage(), e);
			throw new RuntimeException("No se pudo enviar el email de activacion. El usuario no ha sido creado.");
		}

		return savedUser;
	}

	@Transactional
	public void activateUserByToken(String token) {
		User user = userRepository.findByActivationToken(token)
				.orElseThrow(() -> new RuntimeException("Token de activacion invalido."));

		if (user.isActivated()) {
			return;
		}

		if (user.getActivationTokenExpiresAt() == null || user.getActivationTokenExpiresAt().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("El token de activacion ha expirado.");
		}

		user.setActivated(true);
		user.setActivationToken(null);
		user.setActivationTokenExpiresAt(null);
		userRepository.save(user);
	}

	public void resendActivationEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("No existe un usuario con ese email."));

		if (user.isActivated()) {
			throw new RuntimeException("La cuenta ya esta activada.");
		}

		if (user.getActivationToken() == null || user.getActivationTokenExpiresAt() == null
				|| user.getActivationTokenExpiresAt().isBefore(LocalDateTime.now())) {
			user.setActivationToken(UUID.randomUUID().toString());
			user.setActivationTokenExpiresAt(LocalDateTime.now().plusHours(24));
			userRepository.save(user);
		}

		String activationLink = activationBaseUrl + user.getActivationToken();
		emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activationLink);
	}

}
