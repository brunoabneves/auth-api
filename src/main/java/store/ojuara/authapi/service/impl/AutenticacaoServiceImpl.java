package store.ojuara.authapi.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import store.ojuara.authapi.domain.dto.TokenResponseDTO;
import store.ojuara.authapi.domain.form.AutenticacaoForm;
import store.ojuara.authapi.domain.model.Usuario;
import store.ojuara.authapi.repository.UsuarioRepository;
import store.ojuara.authapi.service.AutenticacaoService;
import store.ojuara.authapi.shared.exception.UnauthorizedException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class AutenticacaoServiceImpl implements AutenticacaoService {

    private final UsuarioRepository repository;

    @Value("${auth.jwt.token.secret}")
    private String jwtSecretKey;

    @Value("${auth.jwt.token.expiration.time}")
    private Integer tempoExpiracaoToken;

    @Value("${auth.jwt.refresh-token.expiration.time}")
    private Integer tempoExpiracaoRefreshToken;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repository.findByLogin(login);
    }

    @Override
    public TokenResponseDTO obterToken(AutenticacaoForm form) {
        var usuario = repository.findByLogin(form.getLogin());

        return TokenResponseDTO
                .builder()
                .token(gerarTokenJwt(usuario, tempoExpiracaoToken))
                .refreshToken(gerarTokenJwt(usuario, tempoExpiracaoRefreshToken))
                .build();
    }

    public String gerarTokenJwt(Usuario usuario, Integer expiracao) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(gerarDataExpiracao(expiracao))
                    .sign(algorithm);

        } catch(JWTCreationException e) {
            throw new RuntimeException("Erro ao tentar gerar token.", e);
        }
    }

    @Override
    public String validarTokenJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    @Override
    public TokenResponseDTO obterRefreshToken(String refreshToken) {
        String login = validarTokenJwt(refreshToken);
        var usuario = repository.findByLogin(login);
        if (usuario == null) {
            throw new UnauthorizedException("UnauthorizedException");
        }
        var autentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(autentication);

        return TokenResponseDTO
                .builder()
                .token(gerarTokenJwt(usuario,tempoExpiracaoToken))
                .refreshToken(gerarTokenJwt(usuario, tempoExpiracaoRefreshToken))
                .build();
    }

    private Instant gerarDataExpiracao(Integer expiracao) {
        return LocalDateTime.now()
                .plusHours(expiracao)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
