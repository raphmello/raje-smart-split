package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.CreateAppUserRequest;
import com.raje.smartsplit.dto.response.AppUserResponse;
import com.raje.smartsplit.entity.AppUser;
import com.raje.smartsplit.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService service;

    @Autowired
    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") Long userId) {
        AppUser entity = service.getUserById(userId);
        return new ResponseEntity<>(new AppUserResponse(entity), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<AppUserResponse> createUser(@RequestBody CreateAppUserRequest request) {
        AppUserResponse group = service.createAppUser(request);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }
}