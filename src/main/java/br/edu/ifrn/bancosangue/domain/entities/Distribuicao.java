package br.edu.ifrn.bancosangue.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Distribuicao {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bolsa_sangue_id", nullable = false)
    private BolsaSangue bolsaSangue;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requisicao_hospital_id", nullable = false)
    private RequisicaoHospital requisicaoHospital;

    private LocalDateTime dataEnvio;

    private String responsavelEnvio;

}
