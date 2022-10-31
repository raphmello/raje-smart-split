package com.raje.smartsplit.dto.request;

import javax.validation.constraints.NotBlank;

public class LoginGoogleRequest {

    @NotBlank
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
