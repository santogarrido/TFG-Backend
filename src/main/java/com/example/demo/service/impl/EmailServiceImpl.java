package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	private final String from;

	public EmailServiceImpl(JavaMailSender mailSender, @Value("${app.mail.from:no-reply@tfg-backend.local}") String from) {
		this.mailSender = mailSender;
		this.from = from;
	}

	@Override
	public void sendActivationEmail(String to, String username, String activationLink) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject("Activa tu cuenta");
		message.setText("Hola " + username + ",\n\n" + "Gracias por registrarte. Para activar tu cuenta pulsa este enlace:\n"
				+ activationLink + "\n\n" + "Si no solicitaste esta cuenta, ignora este correo.");
		mailSender.send(message);
	}
}
