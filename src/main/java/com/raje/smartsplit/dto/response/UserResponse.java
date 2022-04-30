package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
    }
}
