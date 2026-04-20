package com.example.demo.service;

import java.util.List;

import com.example.demo.model.UserDTO;

public interface UserService {

	List<UserDTO> listAllUsers();

	UserDTO getUserById(int id);

	UserDTO getUserByUsername(String username);

	UserDTO updateUser(int id, UserDTO userDTO);

	void deleteUser(int id);

	void activateUser(int id);

	void deactivateUser(int id);

}