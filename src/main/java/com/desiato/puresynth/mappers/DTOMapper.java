package com.desiato.puresynth.mappers;

import com.desiato.puresynth.dtos.UserResponseDTO;
import com.desiato.puresynth.models.User;

import java.util.List;

public class DTOMapper {

    public UserResponseDTO toUserResponseDTO(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getEmail()
        );
    }


}
