package com.desiato.puresynth.mappers;

import com.desiato.puresynth.dtos.UserResponseDTO;
import com.desiato.puresynth.models.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getEmail()
        );
    }


}
