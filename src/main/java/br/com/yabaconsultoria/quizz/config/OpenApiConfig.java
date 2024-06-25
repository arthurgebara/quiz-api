package br.com.yabaconsultoria.quizz.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Quiz API")
                        .description("API para Gerenciamento de Quizzes de Saúde")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Yaba Consultoria")
                                .url("https://www.yabaconsultoria.com.br")
                                .email("contato@yabaconsultoria.com.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(Collections.singletonList(
                        new Server().url("http://localhost:8080").description("Servidor Local")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação Completa")
                        .url("https://github.com/yabaconsultoria/quiz-api"));
    }
}
