package jp.co.axa.apidemo.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SwaggerConstant {

    public static final String API_BASE_PACKAGE = "jp.co.axa.apidemo";
    public static final String API_TITLE = "Employee CRUD API";
    public static final String API_DESCRIPTION = "This is an API for CRUD operations on employee data stored in an H2 database. " +
            "The API provides endpoints for GET, POST, DELETE, and PUT operations. " +
            "Access to the API is controlled by role-based authorization and requires an API Key (JWT Token) for access." +
            "<br><div style=\"display: table; background-color: LightGray; border-spacing: 3px;\"> " +
            "  <p style=\"padding: 2px;\"><b>**Note**</b>: This API requires an API_KEY (JWT Token). You can generate a JWT Token in the \"JWT Token Generator\" section below. " +
            "  To generate a token, provide your username and password in a JSON POST request and retrieve the generated JWT token in the response body. " +
            "  Copy the value from <code style=\"color: crimson;\">\"Bearer ......\"</code> and use it as the API Key value to access the Employee API.</p> " +
            "</div>";
    public static final String API_VERSION = "0.0.1-SNAPSHOT";
    public static final String API_TAG_EM = "Employee API";
    public static final String API_TAG_EM_VALUE = "All APIs relating to Employee Information CRUD";
    public static final String API_TAG_AUTH = "JWT Token Generator";
    public static final String API_TAG_AUTH_VALUE = "Generate an API Key for accessing the Employee APIs";
    public static final String TERM_OF_SERVICE = "Your term of service will go here";
    public static final String LICENSE = "Copyrights@axa.co.jp";
    public static final String LICENSE_URL = "https://www.axa.co.jp/license";
    public static final String CONTACT_NAME = "Axa Japan";
    public static final String CONTACT_URL = "https://www.axa.co.jp";
    public static final String CONTACT_EMAIL = "dev@axa.co.jp";

    public static final String SECURITY_REFERENCE = "JWT Token";
    public static final String SECURITY_PASSAS = "header";
    public static final String AUTHORIZATION_SCOPE = "global";
    public static final String AUTHORIZATION_DESCRIPTION = "accessEverything";
    public static final String AUTHORIZATION_HEADER = "Authorization";

}
