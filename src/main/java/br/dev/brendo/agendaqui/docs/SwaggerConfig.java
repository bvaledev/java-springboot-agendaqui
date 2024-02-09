package br.dev.brendo.agendaqui.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("AgendAqui API")
                        .description("API para agendamento de consultas")
                        .version("1.0.0")
        );
    }
}
