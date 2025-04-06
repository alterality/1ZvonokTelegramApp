package odin.zvonok.auth_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import odin.zvonok.auth_service.dto.response.CurrentUserResponse;
import odin.zvonok.auth_service.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final UserService userService;

    @GetMapping("/unsecured")
    public String unsecuredData() {
        return "Сюда можно получить токен без авторизации";
    }

    @GetMapping("/secured")
    public String securedData() {
        return "Сюда можно получить доступ только с валидным jwt-токеном";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminData() {
        return "Сюда может получить доступ только администратор";
    }

    @GetMapping("/current-user")
    public CurrentUserResponse userData() {
        return userService.getCurrentUserInfo();
    }
}