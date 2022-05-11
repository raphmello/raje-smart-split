package com.raje.smartsplit.config.OpenApi30;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApi30Config {

    @Value("${swagger.server.url}")
    private String swaggerUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")))
                .info(new Info().title("App API").version("snapshot"))
                .addSecurityItem(
                        new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")))
                .servers(createServers());
    }

    private List<Server> createServers() {
        List<Server> servers = new ArrayList<>();
        Server server = new Server();
        server.setDescription("QA");
        server.setUrl(swaggerUrl);
        servers.add(server);
        return servers;
    }
}
