package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.request.CreateAppUserRequest;
import com.raje.smartsplit.dto.response.AppUserResponse;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.entity.AppUser;
import com.raje.smartsplit.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository repository;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    public AppUserResponse createAppUser(CreateAppUserRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        AppUser savedUser = repository.save(user);
        return new AppUserResponse(savedUser);
    }

    public AppUserResponse getUserById(Long userId) {
        Optional<AppUser> optional = repository.findById(userId);
        if (optional.isPresent())
            return new AppUserResponse(optional.get());
        throw new RuntimeException("Id not found.");
    }
}
