package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.request.CreateAppUserRequest;
import com.raje.smartsplit.dto.response.AppUserResponse;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.entity.AppUser;
import com.raje.smartsplit.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public AppUser getUserById(Long userId) {
        Optional<AppUser> optional = repository.findById(userId);
        if (optional.isEmpty())
            throw new RuntimeException("User not fount");
        return optional.get();
    }

    public List<AppUser> findAll() {
        return repository.findAll();
    }
}
