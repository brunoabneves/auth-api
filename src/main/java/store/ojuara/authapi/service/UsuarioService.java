package store.ojuara.authapi.service;

import store.ojuara.authapi.domain.dto.UsuarioDTO;
import store.ojuara.authapi.domain.form.UsuarioForm;

public interface UsuarioService {
    UsuarioDTO cadastrar(UsuarioForm form);
}
