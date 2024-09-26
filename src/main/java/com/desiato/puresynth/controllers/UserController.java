package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.dtos.UserRequestDTO;
import com.desiato.puresynth.dtos.UserResponseDTO;
import com.desiato.puresynth.mappers.DTOMapper;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.SessionService;
import com.desiato.puresynth.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final DTOMapper dtoMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserDetailsByToken(
            @RequestHeader("authToken") PureSynthToken pureSynthToken) {

        return sessionService.findUserByToken(pureSynthToken)
                .map(user -> ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getEmail())))
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired token."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponseDTO userResponseDTO = dtoMapper.toUserResponseDTO(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {

        User newUser = new User(userRequestDTO.email(), passwordEncoder.encode(userRequestDTO.password()) );
        User createdUser = userService.saveUser(newUser);

        UserResponseDTO userResponseDTO = new UserResponseDTO(createdUser.getId(), createdUser.getEmail());
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO) {

        User updatedUser = userService.updateUser(id, userRequestDTO);

        UserResponseDTO userResponseDto = dtoMapper.toUserResponseDTO(updatedUser);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
