package odin.zvonok.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import odin.zvonok.auth_service.domain.Role;
import odin.zvonok.auth_service.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
