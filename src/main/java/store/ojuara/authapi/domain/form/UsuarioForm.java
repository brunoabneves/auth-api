package store.ojuara.authapi.domain.form;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioForm {

    @NotNull(message = "Este é um campo obrigatório.")
    String nome;
    @NotNull(message = "Este é um campo obrigatório.")
    String login;
    @NotNull(message = "Este é um campo obrigatório.")
    String senha;
}
