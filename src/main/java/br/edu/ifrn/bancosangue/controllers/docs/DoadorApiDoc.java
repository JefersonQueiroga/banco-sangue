package br.edu.ifrn.bancosangue.controllers.docs;

import br.edu.ifrn.bancosangue.domain.entities.Doador;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Doadores", description = "Gerenciamento de doadores de sangue")
public interface DoadorApiDoc {

    @Operation(
            summary = "Criar novo doador",
            description = "Cadastra um novo doador no sistema com validação de idade (16-69 anos) e CPF único"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doador criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    Doador createDoador(@Valid @RequestBody Doador doador);

    @Operation(summary = "Listar todos os doadores")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    List<Doador> getAllDoadores();

    @Operation(summary = "Buscar doador por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doador encontrado"),
            @ApiResponse(responseCode = "422", description = "Doador não encontrado")
    })
    Doador getDoadorById(@Parameter(description = "ID do doador") @PathVariable Long id);

    @Operation(summary = "Atualizar doador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doador atualizado"),
            @ApiResponse(responseCode = "422", description = "Doador não encontrado")
    })
    Doador updateDoador(
            @Parameter(description = "ID do doador") @PathVariable Long id,
            @Valid @RequestBody Doador dadosAtualizados
    );

    @Operation(summary = "Deletar doador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doador deletado"),
            @ApiResponse(responseCode = "422", description = "Doador não encontrado")
    })
    void deleteDoador(@Parameter(description = "ID do doador") @PathVariable Long id);
}