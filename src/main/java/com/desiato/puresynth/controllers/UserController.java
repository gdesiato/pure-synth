package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.dtos.UserRequestDTO;
import com.desiato.puresynth.dtos.UserResponseDTO;
import com.desiato.puresynth.mappers.UserMapper;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.SessionService;
import com.desiato.puresynth.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserMapper dtoMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserDetailsByToken(
            @RequestHeader("authToken") PureSynthToken pureSynthToken) {

        return sessionService.findUserByToken(pureSynthToken)
                .map(user -> ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getEmail())))
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired token."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {

        User user = userService.getUserByIdOrThrow(id);
        UserResponseDTO userResponseDTO = dtoMapper.toDTO(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {

        User createdUser = userService.createUser(userRequestDTO.email(), userRequestDTO.password());

        UserResponseDTO userResponseDTO = dtoMapper.toDTO(createdUser);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO) {

        User updatedUser = userService.updateUser(id, userRequestDTO);

        UserResponseDTO userResponseDto = dtoMapper.toDTO(updatedUser);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
