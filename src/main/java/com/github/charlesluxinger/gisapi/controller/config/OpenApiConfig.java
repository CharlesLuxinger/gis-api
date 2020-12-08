package com.github.charlesluxinger.gisapi.controller.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@OpenAPIDefinition(
        info = @Info(
                title = "MS GIS API",
                version = "v1.0.0",
                description = "GIS API Challenge",
                contact = @Contact(name = "Charles Luxinger",
                                   email = "charlesluxinger@gmail.com",
                                   url = "https://github.com/CharlesLuxinger/gis-api")
        ),
        servers = @Server(url = "http://localhost:9000/api/v1", description = "Dev Server")
)
public class OpenApiConfig {}
