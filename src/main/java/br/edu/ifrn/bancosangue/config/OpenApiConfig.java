package br.edu.ifrn.bancosangue.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.url:http://localhost:8080}")
    private String serverUrl;

    @Value("${server.description:Servidor de Desenvolvimento}")
    private String serverDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList<>();

        // Servidor atual (baseado no profile ativo)
        servers.add(new Server()
                .url(serverUrl)
                .description(serverDescription));

        return new OpenAPI()
                .info(new Info()
                        .title("API Banco de Sangue")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento de banco de sangue.
                                
                                Funcionalidades principais:
                                - Cadastro e gestão de doadores
                                - Controle de endereços
                                - Triagem de doadores
                                - Gerenciamento de coletas
                                - Controle de bolsas de sangue
                                - Exames de bolsas
                                - Requisições hospitalares
                                - Distribuição de sangue
                                
                                Desenvolvido pelo IFRN.
                                """)
                        .contact(new Contact()
                                .name("Equipe IFRN - Banco de Sangue")
                                .email("jeferson.queiroga@ifrn.edu.br")
                                .url("https://ifrn.edu.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(servers);
    }
}