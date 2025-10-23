package br.edu.ifrn.bancosangue.domain.entities;

import br.edu.ifrn.bancosangue.domain.enums.Sexo;
import br.edu.ifrn.bancosangue.domain.enums.TipoSanguineo;
import jakarta.persistence.*;
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

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    private TipoSanguineo tipoSanguineo;
    private String telefone;
    private String email;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
    private LocalDate dataNascimento;

    @CreatedDate
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;
}
