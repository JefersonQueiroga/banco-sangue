package br.edu.ifrn.bancosangue.domain.entities;

import br.edu.ifrn.bancosangue.domain.enums.ResultadoExameEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
public class ExameBolsa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bolsa_id", nullable = false)
    private BolsaSangue bolsaSangue;

    @Enumerated(EnumType.STRING)
    private ResultadoExameEnum hiv;

    @Enumerated(EnumType.STRING)
    private ResultadoExameEnum hcv;

    @Enumerated(EnumType.STRING)
    private ResultadoExameEnum hbv;

    @Enumerated(EnumType.STRING)
    private ResultadoExameEnum sifilis;

    @Enumerated(EnumType.STRING)
    private ResultadoExameEnum chagas;

    private Boolean liberadoParaUso;

    @Column(name = "data_resultado", nullable = false)
    private LocalDateTime dataResultado;

    @CreatedDate
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;


    @PreUpdate
    public void preUpdate() {
        avaliarLiberacao();
        atualizadoEm = LocalDateTime.now();
    }

    private void avaliarLiberacao() {
        boolean naoReagente =
                hiv == ResultadoExameEnum.NAO_REAGENTE &&
                        hcv == ResultadoExameEnum.NAO_REAGENTE &&
                        hbv == ResultadoExameEnum.NAO_REAGENTE &&
                        sifilis == ResultadoExameEnum.NAO_REAGENTE &&
                        chagas == ResultadoExameEnum.NAO_REAGENTE;

        this.liberadoParaUso = naoReagente;
    }

}
