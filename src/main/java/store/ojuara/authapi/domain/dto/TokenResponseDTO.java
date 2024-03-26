package store.ojuara.authapi.domain.dto;

import lombok.Builder;

@Builder
public record TokenResponseDTO(String token, String refreshToken) {
}
