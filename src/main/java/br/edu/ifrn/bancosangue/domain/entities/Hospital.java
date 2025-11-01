package br.edu.ifrn.bancosangue.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String endereco;

    @Column(columnDefinition = "boolean default true")
    private Boolean ativo;

}
