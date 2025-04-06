package odin.zvonok.auth_service.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import odin.zvonok.auth_service.dto.request.LoginRequest;
import odin.zvonok.auth_service.dto.request.RegistrationUserRequest;
import odin.zvonok.auth_service.dto.request.UpdateTokenRequest;
import odin.zvonok.auth_service.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody LoginRequest authRequest) {
        Map<String, String> tokens = authService.createAuthToken(authRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get("accessToken"))
                .header("Refresh-Token", tokens.get("refreshToken"))
                .build();
    }

    @PostMapping("/refresh-tokens")
    public ResponseEntity<?> attemptToRefreshToken(@RequestBody UpdateTokenRequest updateTokenRequest)
            throws AuthException {
        Map<String, String> tokens = authService.attemptToRefreshTokens(updateTokenRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get("accessToken"))
                .header("Refresh-Token", tokens.get("refreshToken"))
                .build();
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody
                                           RegistrationUserRequest registrationUserRequest) {
        return authService.createNewUser(registrationUserRequest);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
}