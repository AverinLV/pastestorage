package com.example.pastestorage.rest.controllers;

import com.example.pastestorage.dto.mapper.UserMapper;
import com.example.pastestorage.dto.request.CreateUserDTO;
import com.example.pastestorage.dto.request.UserAuthDTO;
import com.example.pastestorage.dto.response.UserSignedUpResponseDTO;
import com.example.pastestorage.models.User;
import com.example.pastestorage.security.JWTUtil;
import com.example.pastestorage.services.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
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
    private final SignUpService signUpService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PostMapping("/sign_up")
    public ResponseEntity<UserSignedUpResponseDTO> performSignUp(@RequestBody @Valid CreateUserDTO createUserDTO) {
        User newUser = userMapper.toUser(createUserDTO);
        signUpService.signUp(newUser);
        UserSignedUpResponseDTO userSignedUpResponseDTO = userMapper.toUserSignedUpResponseDTO(newUser);
        return new ResponseEntity<>(userSignedUpResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserSignedUpResponseDTO> performLogin(@RequestBody @Valid UserAuthDTO userAuthDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(userAuthDTO.getUsername(),
                        userAuthDTO.getPassword());
        authenticationManager.authenticate(authInputToken);
        String token = jwtUtil.generateToken(userAuthDTO.getUsername());
        UserSignedUpResponseDTO userSignedUpResponseDTO = userMapper.toUserSignedUpResponseDTO(
                userAuthDTO.getUsername(),
                token);
        return new ResponseEntity<>(userSignedUpResponseDTO, HttpStatus.OK);
    }
}
