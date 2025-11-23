package br.edu.ifrn.bancosangue.dtos;

import br.edu.ifrn.bancosangue.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de registro de novo usuário.
 * Contém todos os dados necessários para criar uma nova conta.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para registro de novo usuário")
public class RegisterRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(description = "Email do usuário", example = "joao.silva@example.com")
    private String email;

    @Schema(description = "Telefone de contato", example = "(84) 98888-8888")
    private String telefone;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "senha123")
    private String senha;

    @NotNull(message = "Role é obrigatória")
    @Schema(description = "Papel/permissão do usuário no sistema", example = "USER")
    private Role role;
}
