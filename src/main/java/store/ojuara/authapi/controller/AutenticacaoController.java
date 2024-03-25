package store.ojuara.authapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ojuara.authapi.domain.form.AutenticacaoForm;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AutenticacaoController {

    private AuthenticationManager authenticationManager;
    @PostMapping
    public ResponseEntity<String> autenticar(@RequestBody AutenticacaoForm form) {

        var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(form.getLogin(), form.getSenha());
        authenticationManager.authenticate(usuarioAutenticationToken);
        return ResponseEntity.ok("token ...");
    }
}
