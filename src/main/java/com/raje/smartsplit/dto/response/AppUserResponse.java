package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppUserResponse {
    private Long id;
    private String username;

    public AppUserResponse(AppUser entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
    }
}
