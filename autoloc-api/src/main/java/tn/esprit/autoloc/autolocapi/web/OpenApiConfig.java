package tn.esprit.autoloc.autolocapi.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI autolocOpenApi() {
        return new OpenAPI().openapi("3.1.1").info(new Info()
                .title("AutoLoc API")
                .description("API REST de gestion de location de véhicules")
                .version("1.0.0"));
    }
}
