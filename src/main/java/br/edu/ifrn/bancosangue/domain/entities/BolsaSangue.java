package br.edu.ifrn.bancosangue.domain.entities;

import br.edu.ifrn.bancosangue.domain.enums.StatusBolsaSangue;
import br.edu.ifrn.bancosangue.domain.enums.TipoSanguineo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor@NoArgsConstructor
@Getter@Setter
public class BolsaSangue {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String codigoBolsa;

    @Enumerated(EnumType.STRING)
    private TipoSanguineo tipoSanguineo;

    @Enumerated(EnumType.STRING)
    private StatusBolsaSangue status;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    private String localizacao;

    private String observacoes;



}
