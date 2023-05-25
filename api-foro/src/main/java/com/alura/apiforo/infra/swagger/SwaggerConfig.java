package com.alura.apiforo.infra.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Foro")
                        .version("1.0.0")
                        .description("Esta API proporciona servicios para gestionar un foro permitiendo crear, editar y eliminar" +
                                " tópicos y respuestas. Además te permitirá administrar los usuarios que realizan las operaciones" +
                                "y a su vez tener todo seguro ya que se emplea JWT para validar el acceso.")
                        .contact(new Contact()
                                .name("Repositorio del proyecto")
                                .url("https://github.com/ccriollohuaman/challenge-foro-ONE")));
    }
}