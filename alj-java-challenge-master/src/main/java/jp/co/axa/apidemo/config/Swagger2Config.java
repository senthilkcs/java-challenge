package jp.co.axa.apidemo.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static jp.co.axa.apidemo.constant.SwaggerConstant.*;

/**
 * Configuration class for Swagger2 API documentation
 */
@Configuration
public class Swagger2Config {

    /**
     * Configures and returns a Docket object for Swagger2 API documentation
     */
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .tags(new Tag(API_TAG_EM, API_TAG_EM_VALUE),
                        new Tag(API_TAG_AUTH, API_TAG_AUTH_VALUE))
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(Predicates.and(RequestHandlerSelectors.basePackage(API_BASE_PACKAGE)))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(API_TITLE, API_DESCRIPTION, API_VERSION, TERM_OF_SERVICE,
                contact(), LICENSE, LICENSE_URL, Collections.emptyList());
    }

    private Contact contact() {
        return new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL);
    }

    private ApiKey apiKey() {
        return new ApiKey(SECURITY_REFERENCE, AUTHORIZATION_HEADER, SECURITY_PASSAS);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope(AUTHORIZATION_SCOPE, AUTHORIZATION_DESCRIPTION);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(SECURITY_REFERENCE, authorizationScopes));
    }
}

