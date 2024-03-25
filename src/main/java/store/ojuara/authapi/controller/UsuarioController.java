package store.ojuara.authapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import store.ojuara.authapi.domain.dto.UsuarioDTO;
import store.ojuara.authapi.domain.form.UsuarioForm;
import store.ojuara.authapi.service.UsuarioService;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UsuarioController {

    private UsuarioService service;

    @Operation(summary = "Cadastrar usuário", description = "Cadastra um usuário")
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody @Valid UsuarioForm form, UriComponentsBuilder uriBuilder){
        var dto = service.cadastrar(form);
        URI uri = uriBuilder.path("/usuarios/{uuid}").buildAndExpand(dto.uuid()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok("Permissão de administrador.");
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Permissão de usuário.");
    }
}
