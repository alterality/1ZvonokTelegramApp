package odin.zvonok.auth_service.service;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import odin.zvonok.auth_service.auth.CustomUserDetails;
import odin.zvonok.auth_service.auth.SecurityConstants;
import odin.zvonok.auth_service.domain.RefreshToken;
import odin.zvonok.auth_service.domain.User;
import odin.zvonok.auth_service.dto.request.LoginRequest;
import odin.zvonok.auth_service.dto.request.RegistrationUserRequest;
import odin.zvonok.auth_service.dto.request.UpdateTokenRequest;
import odin.zvonok.auth_service.dto.response.UserResponse;
import odin.zvonok.auth_service.exception.AppError;
import odin.zvonok.auth_service.util.JwtTokenUtils;

import java.util.Map;

/**
 * Сервис для аутентификации и управления токенами.
 * <p>
 * Этот сервис отвечает за создание и обновление JWT токенов для пользователей,
 * а также за регистрацию новых пользователей. Он использует различные компоненты,
 * такие как UserService, JwtTokenUtils и RefreshTokenService для выполнения своей работы.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final JwtTokenUtils jwtTokenUtils;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private final SecurityConstants securityConstants;

    public Map<String, String> createAuthToken(@RequestBody LoginRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                        authRequest.getPassword()));

        CustomUserDetails userDetails =
                userService.loadUserByUsername(authRequest.getUsername());

        var accessToken = jwtTokenUtils.generateAccessToken(userDetails);
        var refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);

        refreshTokenService.save(new RefreshToken(refreshToken, userDetails.getId()));

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    public ResponseEntity<?> createNewUser(
            @RequestBody RegistrationUserRequest registrationUserRequest) {
        if (!registrationUserRequest.getPassword()
                .equals(registrationUserRequest.getConfirmPassword())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.findByUsername(registrationUserRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.createNewUser(registrationUserRequest);

        return ResponseEntity.ok(
                new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Transactional
    public Map<String, String> attemptToRefreshTokens(UpdateTokenRequest updateTokenRequest)
            throws AuthException {
        var oldRefreshToken = updateTokenRequest.getRefreshToken();

        if (!refreshTokenService.existsByToken(oldRefreshToken)) {
            throw new AuthException("Переданный refresh-токен не действителен");
        }

        CustomUserDetails userDetails =
                userService.loadUserByUsername(
                        jwtTokenUtils.getUsername(oldRefreshToken,
                                securityConstants.getRefreshSecret()));

        var accessToken = jwtTokenUtils.generateAccessToken(userDetails);
        var refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);

        refreshTokenService.deleteByToken(oldRefreshToken);
        refreshTokenService.save(new RefreshToken(refreshToken, userDetails.getId()));

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );


    }
    @Transactional
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails userDetails) {
                refreshTokenService.deleteByUserId(userDetails.getId());
            } else {
                log.warn("Unexpected principal type: {}", principal.getClass());
            }

            SecurityContextHolder.clearContext();
        }
    }
}