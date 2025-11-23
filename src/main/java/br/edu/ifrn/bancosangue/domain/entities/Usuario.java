package br.edu.ifrn.bancosangue.domain.entities;

import br.edu.ifrn.bancosangue.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Entidade que representa um usuário do sistema.
 * Implementa UserDetails para integração com Spring Security.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo do usuário
     */
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    /**
     * Email do usuário - usado como username para autenticação
     */
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Telefone de contato do usuário
     */
    @Column(length = 15)
    private String telefone;

    /**
     * Senha criptografada do usuário
     */
    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false)
    private String senha;

    /**
     * Role (papel/permissão) do usuário no sistema
     */
    @NotNull(message = "Role é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Indica se a conta do usuário está ativa
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    /**
     * Indica se a conta do usuário não está expirada
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean accountNonExpired = true;

    /**
     * Indica se a conta do usuário não está bloqueada
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean accountNonLocked = true;

    /**
     * Indica se as credenciais do usuário não estão expiradas
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean credentialsNonExpired = true;

    /**
     * Data e hora de criação do registro
     */
    @CreatedDate
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    /**
     * Data e hora da última modificação do registro
     */
    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // ==================== Métodos do UserDetails ====================

    /**
     * Retorna as authorities (permissões) do usuário baseadas em sua role.
     * Usado pelo Spring Security para controle de acesso.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    /**
     * Retorna a senha criptografada do usuário.
     * Usado pelo Spring Security para autenticação.
     */
    @Override
    public String getPassword() {
        return senha;
    }

    /**
     * Retorna o username usado para autenticação (email neste caso).
     * Usado pelo Spring Security para autenticação.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indica se a conta do usuário não está expirada.
     * Usado pelo Spring Security para validação de conta.
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Indica se a conta do usuário não está bloqueada.
     * Usado pelo Spring Security para validação de conta.
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Indica se as credenciais do usuário não estão expiradas.
     * Usado pelo Spring Security para validação de credenciais.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * Indica se a conta do usuário está ativa.
     * Usado pelo Spring Security para validação de conta.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // ==================== Lifecycle Callbacks ====================

    /**
     * Define a data de cadastro antes de persistir a entidade.
     */
    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
        if (enabled == null) {
            enabled = true;
        }
        if (accountNonExpired == null) {
            accountNonExpired = true;
        }
        if (accountNonLocked == null) {
            accountNonLocked = true;
        }
        if (credentialsNonExpired == null) {
            credentialsNonExpired = true;
        }
    }

    /**
     * Atualiza a data de atualização antes de atualizar a entidade.
     */
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
