package com.lara.securecapita.service.implementation;

import com.lara.securecapita.domain.User;
import com.lara.securecapita.dto.UserDTO;
import com.lara.securecapita.dtomapper.UserDTOMapper;
import com.lara.securecapita.repository.UserRepository;
import com.lara.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper
                .fromUser(userRepository.create(user));
    }

}
