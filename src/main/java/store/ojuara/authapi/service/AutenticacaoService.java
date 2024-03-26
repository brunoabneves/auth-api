package store.ojuara.authapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import store.ojuara.authapi.domain.dto.TokenResponseDTO;
import store.ojuara.authapi.domain.form.AutenticacaoForm;

public interface AutenticacaoService extends UserDetailsService {
    TokenResponseDTO obterToken(AutenticacaoForm form);
    String validarTokenJwt(String token);
    TokenResponseDTO obterRefreshToken(String refreshToken);
}
