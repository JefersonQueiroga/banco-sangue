package br.edu.ifrn.bancosangue.domain.entities;

import br.edu.ifrn.bancosangue.domain.enums.TipoSanguineo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class RequisicaoHospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraRequisicao;

    @Enumerated(EnumType.STRING)
    private TipoSanguineo tipoSanguineoNecessario;

    private Integer quantidadeBolsasNecessarias;

    private Integer quantidadeBolsasAtendidas;

}
