package com.lara.securecapita.service;

import com.lara.securecapita.domain.User;
import com.lara.securecapita.dto.UserDTO;

public interface UserService {

    UserDTO createUser(User user);

    UserDTO getUserByEmail(String email);

}
