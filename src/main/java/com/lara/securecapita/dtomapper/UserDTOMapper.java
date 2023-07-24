package com.lara.securecapita.dtomapper;

import com.lara.securecapita.domain.User;
import com.lara.securecapita.dto.UserDTO;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserDTOMapper {

    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

}
