package store.ojuara.authapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import store.ojuara.authapi.domain.model.Usuario;
import store.ojuara.authapi.repository.UsuarioRepository;
import store.ojuara.authapi.service.AutenticacaoService;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extrairTokenHeader(request);
        if(token != null) {
            String login = autenticacaoService.validarTokenJwt(token);
            Usuario usuario = usuarioRepository.findByLogin(login);

            var autentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(autentication);
        }
        filterChain.doFilter(request, response);
    }

    public String extrairTokenHeader(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if(authHeader == null) {
            return null;
        }
        if(!authHeader.split(" ")[0].equals("Bearer")) {
            return null;
        }
        return authHeader.split(" ")[1];
    }
}
