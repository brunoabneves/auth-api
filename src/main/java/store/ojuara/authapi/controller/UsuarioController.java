package store.ojuara.authapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private ResponseEntity<UsuarioDTO> cadastrar(@RequestBody @Valid UsuarioForm form, UriComponentsBuilder uriBuilder){
        var dto = service.cadastrar(form);
        URI uri = uriBuilder.path("/usuarios/{uuid}").buildAndExpand(dto.uuid()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}
