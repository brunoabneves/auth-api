package store.ojuara.authapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ojuara.authapi.domain.dto.UsuarioDTO;
import store.ojuara.authapi.domain.form.UsuarioForm;
import store.ojuara.authapi.domain.model.Usuario;
import store.ojuara.authapi.mapper.UsuarioMapper;
import store.ojuara.authapi.repository.UsuarioRepository;
import store.ojuara.authapi.service.UsuarioService;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioMapper mapper;
    private final UsuarioRepository repository;

    @Override
    public UsuarioDTO cadastrar(UsuarioForm form) {
        var usuarioExistente = repository.findByLogin(form.getLogin());
        if(usuarioExistente != null) {
            throw new RuntimeException("Usuário já existe.");
        }
        var usuario = mapper.toModel(form);
        return mapper.toDto(repository.save(usuario));
    }
}
