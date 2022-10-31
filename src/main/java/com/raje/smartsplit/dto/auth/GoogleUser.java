package com.raje.smartsplit.dto.auth;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUser {
    private String userId;
    private String email;
    private boolean emailVerified;
    private String name;
    private String pictureUrl;
    private String locale;
    private String familyName;
    private String givenName;
}
