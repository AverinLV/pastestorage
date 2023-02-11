package com.example.pastestorage.rest.controllers;

import com.example.pastestorage.dto.mapper.UserMapper;
import com.example.pastestorage.dto.request.SetUserRoleDTO;
import com.example.pastestorage.dto.response.MessageDTO;
import com.example.pastestorage.dto.response.UserResponseDTO;
import com.example.pastestorage.models.User;
import com.example.pastestorage.services.UserService;
import com.example.pastestorage.types.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    @PostMapping("/{username}/set_role")
    public ResponseEntity<MessageDTO> setRole(@PathVariable("username") String username, @RequestBody SetUserRoleDTO setUserRoleDTO) {
        UserRole role = setUserRoleDTO.getRole();
        userService.setRole(username, role);
        return new ResponseEntity<>(new MessageDTO("Successfully granted " + role.name() + " rights to user with ID " + username), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("username") String username) {
        User user = userService.get(username);
        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(user);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }
}
