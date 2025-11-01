package br.edu.ifrn.bancosangue.domain.entities;

import br.edu.ifrn.bancosangue.domain.enums.Sexo;
import br.edu.ifrn.bancosangue.domain.enums.TipoSanguineo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class Doador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @NotBlank(message = "CPF é obrigatório")
    @Column(name = "cpf", nullable = false, length = 11 , unique = true)
    private String cpf;

    @NotNull(message = "Sexo é obrigatório")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotNull(message = "Tipo sanguíneo é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoSanguineo tipoSanguineo;
    private String telefone;
    private String email;

    @NotNull(message = "Status ativo é obrigatório")
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @CreatedDate
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;


    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
}
