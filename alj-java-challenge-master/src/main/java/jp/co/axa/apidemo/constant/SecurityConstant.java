package jp.co.axa.apidemo.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstant {

    public static final String EMPLOYEE_URL = "/api/v1/employees/**";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String READ_ROLE = "READ";
    public static final String WRITE_ROLE = "WRITE";
}
