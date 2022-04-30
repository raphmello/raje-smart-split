package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.CreateAppUserRequest;
import com.raje.smartsplit.dto.response.AppUserResponse;
import com.raje.smartsplit.entity.AppUser;
import com.raje.smartsplit.service.AppUserService;
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
public class AppUserController {

    private final AppUserService service;

    @Autowired
    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Retreive all users")
    public ResponseEntity<List<AppUserResponse>> getAllUser() {
        List<AppUser> entityList = service.findAll();
        List<AppUserResponse> responseList = new ArrayList<>();
        entityList.forEach(user -> responseList.add(new AppUserResponse(user)));
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find user by id{id}")
    public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") Long userId) {
        AppUser entity = service.getUserById(userId);
        return new ResponseEntity<>(new AppUserResponse(entity), HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(summary = "Create a user")
    public ResponseEntity<AppUserResponse> createUser(@RequestBody @Valid CreateAppUserRequest request) {
        AppUserResponse group = service.createAppUser(request);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }
}
