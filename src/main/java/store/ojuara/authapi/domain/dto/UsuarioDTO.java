package store.ojuara.authapi.domain.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import store.ojuara.authapi.domain.enums.RoleEnum;

import java.util.UUID;

public record UsuarioDTO(
        Long id,
        UUID uuid,
        String nome,
        String login,
        String senha,
        @Enumerated(EnumType.STRING)
        RoleEnum role
) {
}
