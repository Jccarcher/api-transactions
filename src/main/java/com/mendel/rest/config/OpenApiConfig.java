package com.mendel.rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
            title = "Transactions API",
            version = "1.0",
            description = """
                    Mendel - api-transactions
                    
                    Este proyecto es una **API REST de ejemplo** para gestionar transacciones en memoria usando Spring Boot, H2 y JPA.
                    
                    ## Tecnologías principales
                    - Spring Boot 4
                    - Spring Data JPA (Jakarta Persistence)
                    - H2 Database (in-memory) con consola web
                    - Spring Boot Actuator (/actuator/health)
                    - Lombok
                    
                    ## Estructura básica
                    * com.mendel.rest.controllers - controladores web
                    * com.mendel.rest.models - entidades JPA
                    * com.mendel.rest.services - lógica de negocio
                    * com.mendel.rest.repository - repositorios Spring Data
                    
                    ## Configuración
                    - **Base de datos:** H2 en memoria
                    - **Consola H2:** Disponible en /h2-mendel (usuario: sa, sin contraseña)
                    - **Actuator:** Disponible en /actuator/health
                    - **Ejecución:** mvn spring-boot:run o docker-compose up --build
                    
                    ## Características
                    - Endpoint de salud simple: GET /health
                    - Actuator health: GET /actuator/health
                    - Documentación interactiva: disponible en /swagger-ui/index.html
                    - Especificación OpenAPI: disponible en /v3/api-docs
                    """,
            contact = @Contact(name = "Mendel Team", email = "support@mendel.com")
    )
)
public class OpenApiConfig {
    // additional configuration can be provided here if needed
}
