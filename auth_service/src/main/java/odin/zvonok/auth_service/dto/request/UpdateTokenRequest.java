package odin.zvonok.auth_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTokenRequest {
    private String refreshToken;
}
