package com.raje.smartsplit.controller;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.auth.GoogleUser;
import com.raje.smartsplit.dto.request.LoginGoogleRequest;
import com.raje.smartsplit.dto.request.LoginRequest;
import com.raje.smartsplit.dto.request.SignupRequest;
import com.raje.smartsplit.dto.response.JwtResponse;
import com.raje.smartsplit.dto.response.MessageResponse;
import com.raje.smartsplit.entity.Role;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.entity.UserDetailsImpl;
import com.raje.smartsplit.enums.ERole;
import com.raje.smartsplit.repository.RoleRepository;
import com.raje.smartsplit.repository.UserRepository;
import com.raje.smartsplit.service.auth.GoogleAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = AuthController.MAX_AGE)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

    public static final int MAX_AGE = 3600;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final GoogleAuthService googleAuthService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder encoder,
                          JwtUtils jwtUtils, GoogleAuthService googleAuthService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        return createResponseWithJwt(authentication);
    }

    @PostMapping("/signin/google")
    public ResponseEntity<?> authenticateUserWithGoogle(@Valid @RequestBody LoginGoogleRequest loginRequest) {

        GoogleUser googleUser = googleAuthService.validateGoogleToken(loginRequest.getIdToken());

        Optional<User> user = userRepository.findByUsername(googleUser.getEmail());

        if (user.isEmpty()) {
            signUpIfUserNotFound(googleUser);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(googleUser.getEmail(), googleUser.getUserId()));
        return createResponseWithJwt(authentication);
    }

    private void signUpIfUserNotFound(GoogleUser googleUser) {
        User user = new User(googleUser.getEmail(),
                googleUser.getEmail(),
                encoder.encode(googleUser.getUserId()));
        registerNewUser(user, Set.of("user"));
    }

    private ResponseEntity<?> createResponseWithJwt(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already registered!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        registerNewUser(user, strRoles);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private void registerNewUser(User user, Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
    }
}
