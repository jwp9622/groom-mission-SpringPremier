package com.example.SpringPremier.domain.auth.role;

import java.util.Arrays;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_USER("ROLE_USER", "일반회원");

    private final String code;   // DB 저장값 or 요청값
    private final String label;  // 사용자에게 보여줄 이름

    Role(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    // code → Enum
    public static Role fromCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + code));
    }
}
