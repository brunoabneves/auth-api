package store.ojuara.authapi.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import store.ojuara.authapi.domain.form.AutenticacaoForm;
import store.ojuara.authapi.domain.model.Usuario;
import store.ojuara.authapi.repository.UsuarioRepository;
import store.ojuara.authapi.service.AutenticacaoService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class AutenticacaoServiceImpl implements AutenticacaoService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repository.findByLogin(login);
    }

    @Override
    public String obterToken(AutenticacaoForm form) {
        var usuario = repository.findByLogin(form.getLogin());
        return gerarTokenJwt(usuario);
    }

    public String gerarTokenJwt(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);

        } catch(JWTCreationException e) {
            throw new RuntimeException("Erro ao tentar gerar token.", e);
        }
    }

    @Override
    public String validarTokenJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusMinutes(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
