package com.example.pastestorage.rest.controllers;

import com.example.pastestorage.dto.mapper.UserMapper;
import com.example.pastestorage.dto.request.CreateUserDTO;
import com.example.pastestorage.dto.request.UserAuthDTO;
import com.example.pastestorage.dto.response.UserAuthenticatedResponseDTO;
import com.example.pastestorage.models.User;
import com.example.pastestorage.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class AuthController {
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    @PostMapping("/sign_up")
    public ResponseEntity<UserAuthenticatedResponseDTO> performSignUp(@RequestBody @Valid CreateUserDTO createUserDTO) {
        User newUser = userMapper.toUser(createUserDTO);
        authenticationService.signUp(newUser);
        UserAuthenticatedResponseDTO userAuthenticatedResponseDTO = userMapper.toUserSignedUpResponseDTO(newUser);
        return new ResponseEntity<>(userAuthenticatedResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthenticatedResponseDTO> performLogin(@RequestBody @Valid UserAuthDTO userAuthDTO) {
        String token = authenticationService.loginAndGetToken(userAuthDTO);
        UserAuthenticatedResponseDTO userAuthenticatedResponseDTO = userMapper.toUserSignedUpResponseDTO(
                userAuthDTO.getUsername(),
                token);
        return new ResponseEntity<>(userAuthenticatedResponseDTO, HttpStatus.OK);
    }
}
