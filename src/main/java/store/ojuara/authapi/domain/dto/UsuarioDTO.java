package store.ojuara.authapi.domain.dto;

import java.util.UUID;

public record UsuarioDTO(
        Long id,
        UUID uuid,
        String nome,
        String login,
        String senha
) {
}
