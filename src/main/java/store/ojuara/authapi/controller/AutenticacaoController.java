package store.ojuara.authapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import store.ojuara.authapi.domain.dto.TokenResponseDTO;
import store.ojuara.authapi.domain.form.AutenticacaoForm;
import store.ojuara.authapi.service.AutenticacaoService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AutenticacaoController {

    private AuthenticationManager authenticationManager;
    private AutenticacaoService autenticacaoService;

    @PostMapping
    public ResponseEntity<TokenResponseDTO> autenticar(@RequestBody AutenticacaoForm form) {
        var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(form.getLogin(), form.getSenha());
        authenticationManager.authenticate(usuarioAutenticationToken);
        return ResponseEntity.ok(autenticacaoService.obterToken(form));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDTO> authRefreshToken(@RequestParam(value = "refreshToken", required = true) String refreshToken) {
        return ResponseEntity.ok(autenticacaoService.obterRefreshToken(refreshToken));
    }
}
