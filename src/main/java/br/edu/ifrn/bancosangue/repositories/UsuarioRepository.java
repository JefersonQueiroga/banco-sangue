package br.edu.ifrn.bancosangue.repositories;

import br.edu.ifrn.bancosangue.domain.entities.Usuario;
import br.edu.ifrn.bancosangue.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para gerenciamento de usuários.
 * Fornece métodos para consultas customizadas de usuários.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo email (username).
     * Usado principalmente para autenticação.
     *
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se já existe um usuário com o email informado.
     * Útil para validação durante o cadastro.
     *
     */
    boolean existsByEmail(String email);

    /**
     * Busca todos os usuários com uma role específica.
     *
     * @param role Role a ser buscada
     * @return Lista de usuários com a role especificada
     */
    List<Usuario> findByRole(Role role);

    /**
     * Busca todos os usuários ativos.
     *
     * @param enabled Status de ativação
     * @return Lista de usuários ativos ou inativos
     */
    List<Usuario> findByEnabled(Boolean enabled);

    /**
     * Busca usuários pelo nome (busca parcial case-insensitive).
     *
     * @param nome Nome ou parte do nome a ser buscado
     * @return Lista de usuários encontrados
     */
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}
