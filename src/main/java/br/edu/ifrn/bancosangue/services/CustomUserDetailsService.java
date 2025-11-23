package br.edu.ifrn.bancosangue.services;

import br.edu.ifrn.bancosangue.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementação customizada do UserDetailsService do Spring Security.
 * Responsável por carregar os detalhes do usuário a partir do banco de dados
 * durante o processo de autenticação.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Carrega os detalhes do usuário pelo username (email).
     * Este método é chamado automaticamente pelo Spring Security
     * durante o processo de autenticação.
     *
     * @param username Username (email) do usuário
     * @return UserDetails contendo as informações do usuário
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado com email: " + username
                ));
    }
}
