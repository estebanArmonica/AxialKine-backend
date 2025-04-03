package com.tiendaweb.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "API AxialKine",
                description = "API de axialkine de una tienda de suscripciones web con integracion con transbank",
                termsOfService = "www.tiendaweb.com/terminos_y_servicios",
                version = "1.0.3",
                contact = @Contact(
                        name = "Esteban Hernán Lobos Canales",
                        email = "esteban.hernan.lobos@gmail.com"
                ),
                license = @License(
                        name = "Standar Software Use License for Esteban Hernán Lobos Canales"
                )
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8094"
                ),
                @Server(
                        description = "PROD SERVER",
                        url = "http://tiendawebaxialkine:8094"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Access Token FOr My API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer ",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
