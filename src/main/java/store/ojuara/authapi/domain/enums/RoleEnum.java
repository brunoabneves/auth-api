package store.ojuara.authapi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    ADMIN("admin"),
    USER("user");

    private final String role;
}
