package br.edu.ifrn.bancosangue.domain.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

/**
 * Enum que define as roles (papéis) dos usuários no sistema.
 * Cada role representa um nível de acesso diferente.
 */
public enum Role {

    /**
     * Usuário comum - permissões básicas de leitura
     */
    USER,

    /**
     * Atendente do banco de sangue - pode gerenciar doadores e agendamentos
     */
    ATENDENTE,

    /**
     * Médico - pode realizar triagens, aprovar coletas e gerenciar exames
     */
    MEDICO,

    /**
     * Administrador - acesso completo ao sistema
     */
    ADMIN;

    /**
     * Retorna a lista de authorities do Spring Security baseadas na role.
     * O prefixo ROLE_ é uma convenção do Spring Security.
     *
     * @return Lista contendo a SimpleGrantedAuthority da role
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + this.name())
        );
    }
}
