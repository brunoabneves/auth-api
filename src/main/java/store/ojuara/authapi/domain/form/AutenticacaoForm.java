package store.ojuara.authapi.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutenticacaoForm {

    private String login;
    private String senha;
}
