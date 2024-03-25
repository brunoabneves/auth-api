package store.ojuara.authapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import store.ojuara.authapi.domain.form.AutenticacaoForm;

public interface AutenticacaoService extends UserDetailsService {
    String obterToken(AutenticacaoForm form);
    String validarTokenJwt(String token);
}
