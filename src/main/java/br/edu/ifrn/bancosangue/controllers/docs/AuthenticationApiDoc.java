package br.edu.ifrn.bancosangue.controllers.docs;

import br.edu.ifrn.bancosangue.dtos.AuthResponse;
import br.edu.ifrn.bancosangue.dtos.LoginRequest;
import br.edu.ifrn.bancosangue.dtos.RegisterRequest;
import br.edu.ifrn.bancosangue.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * Interface de documentação para a API de autenticação.
 * Define as anotações do Swagger/OpenAPI para os endpoints de autenticação.
 */
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public interface AuthenticationApiDoc {

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário no sistema e retorna um token JWT para autenticação"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou email já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<AuthResponse> register(RegisterRequest request);

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário com email e senha, retornando um token JWT para acesso à API"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email ou senha inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<AuthResponse> login(LoginRequest request);
}
