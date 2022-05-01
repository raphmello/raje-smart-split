package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.request.SignupRequest;
import com.raje.smartsplit.dto.response.UserResponse;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse createUser(SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        User savedUser = repository.save(user);
        return new UserResponse(savedUser);
    }

    public User getUserById(Long userId) {
        Optional<User> optional = repository.findById(userId);
        if (optional.isEmpty())
            throw new RuntimeException("User not fount");
        return optional.get();
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}
