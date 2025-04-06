package odin.zvonok.auth_service.repository;

import odin.zvonok.auth_service.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    boolean existsRefreshTokenByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(Long userId);
}
