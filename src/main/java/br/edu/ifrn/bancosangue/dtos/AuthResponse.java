package br.edu.ifrn.bancosangue.dtos;

import br.edu.ifrn.bancosangue.domain.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para operações de autenticação (login e register).
 * Contém o token JWT e informações básicas do usuário autenticado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta de autenticação contendo token JWT e dados do usuário")
public class AuthResponse {

    @JsonProperty("access_token")
    @Schema(description = "Token JWT para autenticação",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @JsonProperty("token_type")
    @Schema(description = "Tipo do token", example = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "ID do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário", example = "João Silva")
    private String nome;

    @Schema(description = "Email do usuário", example = "joao.silva@example.com")
    private String email;

    @Schema(description = "Role (papel/permissão) do usuário", example = "USER")
    private Role role;

    @Schema(description = "Mensagem de sucesso", example = "Login realizado com sucesso")
    private String mensagem;
}
