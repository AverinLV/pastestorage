package com.example.pastestorage.rest.controllers;

import com.example.pastestorage.dto.mapper.UserMapper;
import com.example.pastestorage.dto.request.SetUserRoleDTO;
import com.example.pastestorage.dto.response.MessageDTO;
import com.example.pastestorage.dto.response.PageUsersResponseDTO;
import com.example.pastestorage.dto.response.UserResponseDTO;
import com.example.pastestorage.models.User;
import com.example.pastestorage.services.UserService;
import com.example.pastestorage.types.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
@Log4j2
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    @PostMapping("/{username}/set_role")
    public ResponseEntity<MessageDTO> setRole(@PathVariable("username") String username, @RequestBody SetUserRoleDTO setUserRoleDTO) {
        log.info(String.format("Accessed POST users/%s/set_role endpoint", username));
        UserRole role = setUserRoleDTO.getRole();
        userService.setRole(username, role);
        return new ResponseEntity<>(new MessageDTO("Successfully granted " + role.name() + " rights to user with ID " + username), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("username") String username) {
        log.info(String.format("Accessed GET users/%s endpoint", username));
        User user = userService.get(username);
        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(user);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PageUsersResponseDTO> getAllUsers(@RequestParam(required = false, defaultValue = "0") @Min(0) int page,
                                                             @RequestParam(required = false, defaultValue = "10") @Min(0) int size) {
        log.info("Accessed GET users/ endpoint");
        Page<User> pageUsers = userService.getAll(page, size);
        PageUsersResponseDTO pageUsersResponseDTO = userMapper.toPageUsersResponseDTO(pageUsers);
        return new ResponseEntity<>(pageUsersResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable("username") String username) {
        log.info(String.format("Accessed DELETE users/%s endpoint", username));
        User deletedUser = userService.delete(username);
        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(deletedUser);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }
}
