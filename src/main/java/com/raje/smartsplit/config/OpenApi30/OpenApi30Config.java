package com.raje.smartsplit.config.OpenApi30;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApi30Config {
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
        Server serverProd = new Server();
        serverProd.setDescription("QA");
        serverProd.setUrl("https://smart-split-raje-app.herokuapp.com");
        Server serverDev = new Server();
        serverDev.setDescription("DEV");
        serverDev.setUrl("http://localhost:8080");
        servers.add(serverDev);
        servers.add(serverProd);
        return servers;
    }
}
