package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.model.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;

	/**
	 * List all Users
	 */
	@Override
	public List<UserDTO> listAllUsers() {
		List<UserDTO> usersDTO = new ArrayList<>();
		for (User user : userRepository.findAll()) {
			usersDTO.add(transform(user));
		}
		return usersDTO;
	}

	/**
	 * Find User by its Id
	 */
	@Override
	public UserDTO getUserById(int id) {
		UserDTO userDTO = transform(
				userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
		return userDTO;
	}
	
	@Override
	public UserDTO getUserByUsername(String username) {
		UserDTO userDTO = transform(
				userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
		return userDTO;
	}

	/**
	 * Update an existing User
	 */
	@Override
	public UserDTO updateUser(int id, UserDTO userDTO) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		user.setName(userDTO.getName());
		user.setSecondName(userDTO.getSecondName());
		return transform(userRepository.save(user));
	}

	/**
	 * Soft delete an User
	 */
	@Override
	public void deleteUser(int id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		user.setDeleted(true);
		user.setActivated(false);
		userRepository.save(user);
	}

	/**
	 * Activate User
	 */
	@Override
	public void activateUser(int id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		user.setActivated(true);
		userRepository.save(user);
	}

	/**
	 * Deactivate User
	 */
	@Override
	public void deactivateUser(int id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		user.setActivated(false);
		userRepository.save(user);
	}

	/**
	 * Transform entity to model
	 * 
	 * @param court
	 * @return
	 */
	private UserDTO transform(User user) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(user, UserDTO.class);
	}

	/**
	 * Transform model to entity
	 * 
	 * @param courtDTO
	 * @return
	 */
	private User transform(UserDTO userDTO) {

		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(userDTO, User.class);

		return user;

	}

}
