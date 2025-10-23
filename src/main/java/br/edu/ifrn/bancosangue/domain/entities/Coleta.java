package br.edu.ifrn.bancosangue.domain.entities;

import br.edu.ifrn.bancosangue.domain.enums.SituacaoColeta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class Coleta {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Doador doador;

    @ManyToOne
    private Triagem triagem;

    private LocalDate dataColeta;

    private Double volumeMl;

    private String responsavel;

    @Enumerated(EnumType.STRING)
    private SituacaoColeta situacaoColeta;

}
