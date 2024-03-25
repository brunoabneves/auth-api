package store.ojuara.authapi.domain.form;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import store.ojuara.authapi.domain.enums.RoleEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioForm {

    String nome;
    String login;
    String senha;
    @Enumerated(EnumType.STRING)
    RoleEnum role;
}
