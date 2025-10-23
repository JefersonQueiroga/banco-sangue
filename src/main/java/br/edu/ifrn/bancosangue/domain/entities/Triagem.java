package br.edu.ifrn.bancosangue.domain.entities;

import br.edu.ifrn.bancosangue.domain.enums.Aptidao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Triagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doador_id", nullable = false)
    private Doador doador;

    @Column(name = "data_triagem", nullable = false)
    @CreatedDate
    private LocalDateTime dataTriagem;

    @Column(name = "peso_kg", nullable = false)
    private Double pesoKg;

    @Column(name = "altura_cm", nullable = false)
    private Double alturaCm;

    @Column(name = "pressao_arterial", nullable = false)
    private String pressaoArterial;

    private String observacoes;

    @Enumerated(EnumType.STRING)
    private Aptidao aptidao;


}
