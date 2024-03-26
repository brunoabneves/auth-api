package store.ojuara.authapi.mapper;

import org.mapstruct.Mapper;
import store.ojuara.authapi.domain.dto.UsuarioDTO;
import store.ojuara.authapi.domain.form.UsuarioForm;
import store.ojuara.authapi.domain.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario, UsuarioForm>{

    Usuario toModel(UsuarioForm form);
}
