package odin.zvonok.auth_service.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import odin.zvonok.auth_service.service.UserService;
import odin.zvonok.auth_service.util.JwtTokenUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final SecurityConstants securityConstants;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(securityConstants.getAuthHeader());
        var bearerPrefix = securityConstants.getBearerPrefix();
        String username = null;
        String accessToken = null;

        if (authHeader != null && authHeader.startsWith(bearerPrefix)) {
            accessToken = authHeader.substring(bearerPrefix.length());
            try {
                username = jwtTokenUtils.getUsername(accessToken, securityConstants.getAccessSecret());
            } catch (ExpiredJwtException e) {
                log.error("Token expired: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Authentication error: {}", e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = userService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}