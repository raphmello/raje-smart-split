package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.CreateAppUserRequest;
import com.raje.smartsplit.dto.response.UserResponse;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Retreive all users")
    public ResponseEntity<List<UserResponse>> getAllUser() {
        List<User> entityList = service.findAll();
        List<UserResponse> responseList = new ArrayList<>();
        entityList.forEach(user -> responseList.add(new UserResponse(user)));
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find user by id{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long userId) {
        User entity = service.getUserById(userId);
        return new ResponseEntity<>(new UserResponse(entity), HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(summary = "Create a user")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateAppUserRequest request) {
        UserResponse group = service.createAppUser(request);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }
}
