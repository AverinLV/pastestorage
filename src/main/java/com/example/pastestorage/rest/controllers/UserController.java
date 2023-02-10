package com.example.pastestorage.rest.controllers;

import com.example.pastestorage.dto.request.SetUserRoleDTO;
import com.example.pastestorage.dto.response.MessageDTO;
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
    @PostMapping("/{id}/set_role")
    public ResponseEntity<MessageDTO> setRole(@PathVariable("id") long id, @RequestBody SetUserRoleDTO setUserRoleDTO) {
        UserRole role = setUserRoleDTO.getRole();
        userService.setRole(id, role);
        return new ResponseEntity<>(new MessageDTO("Successfully granted " + role.name() + " rights to user with ID " + id), HttpStatus.OK);
    }
}
