package com.example.demo.service;

public interface EmailService {
	void sendActivationEmail(String to, String username, String activationLink);
}
