package com.raje.smartsplit.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.raje.smartsplit.dto.auth.GoogleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {

    Logger logger = LoggerFactory.getLogger(GoogleAuthService.class);

    @Value("${google.sso.client.id.ios}")
    private String CLIENT_ID;

    public GoogleUser validateGoogleToken(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        final GoogleIdToken idToken = getGoogleIdToken(idTokenString, verifier);;

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            return GoogleUser.builder()
                    .userId(userId)
                    .email(payload.getEmail())
                    .emailVerified(payload.getEmailVerified())
                    .name((String) payload.get("name"))
                    .pictureUrl((String) payload.get("picture"))
                    .locale((String) payload.get("locale"))
                    .familyName((String) payload.get("family_name"))
                    .givenName((String) payload.get("given_name"))
                    .build();
        } else {
            logger.error("m=validateGoogleToken label=idToken IS NULL");
            throw new RuntimeException("idToken is null: Not possible to validate Google token ");
        }
    }

    private GoogleIdToken getGoogleIdToken(String idTokenString, GoogleIdTokenVerifier verifier) {
        try {
            return verifier.verify(idTokenString);
        } catch (Exception e) {
            logger.error("m=validateGoogleToken idTokenString={}", idTokenString);
            throw new RuntimeException("Not possible to validate Google idToken");
        }
    }
}
