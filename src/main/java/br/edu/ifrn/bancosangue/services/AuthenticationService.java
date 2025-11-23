package br.edu.ifrn.bancosangue.services;

import br.edu.ifrn.bancosangue.domain.entities.Usuario;
import br.edu.ifrn.bancosangue.dtos.AuthResponse;
import br.edu.ifrn.bancosangue.dtos.LoginRequest;
import br.edu.ifrn.bancosangue.dtos.RegisterRequest;
import br.edu.ifrn.bancosangue.exceptions.BusinessException;
import br.edu.ifrn.bancosangue.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela lógica de autenticação e registro de usuários.
 * Gerencia login, registro e geração de tokens JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra um novo usuário no sistema.
     *
     * Fluxo:
     * 1. Valida se o email já não está em uso
     * 2. Cria a entidade Usuario com senha criptografada
     * 3. Salva no banco de dados
     * 4. Gera token JWT
     * 5. Retorna resposta com token e dados do usuário
     *
     * @param request Dados de registro do usuário
     * @return AuthResponse com token e informações do usuário
     * @throws BusinessException se o email já estiver cadastrado
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Valida se o email já está em uso
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado no sistema");
        }

        // 2. Cria a entidade Usuario
        var usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .senha(passwordEncoder.encode(request.getSenha())) // Criptografa a senha
                .role(request.getRole())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        // 3. Salva no banco de dados
        var savedUsuario = usuarioRepository.save(usuario);

        // 4. Gera o token JWT
        var jwtToken = jwtService.generateToken(usuario);

        // 5. Retorna a resposta
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .id(savedUsuario.getId())
                .nome(savedUsuario.getNome())
                .email(savedUsuario.getEmail())
                .role(savedUsuario.getRole())
                .mensagem("Usuário registrado com sucesso")
                .build();
    }

    /**
     * Autentica um usuário no sistema (login).
     *
     * Fluxo:
     * 1. Autentica as credenciais (email e senha) via AuthenticationManager
     * 2. Se autenticado, busca o usuário no banco
     * 3. Gera token JWT
     * 4. Retorna resposta com token e dados do usuário
     *
     * @param request Credenciais de login
     * @return AuthResponse com token e informações do usuário
     * @throws BadCredentialsException se as credenciais forem inválidas
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            // 1. Tenta autenticar as credenciais
            // O AuthenticationManager usa o UserDetailsService e o PasswordEncoder
            // para validar o email e a senha
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );

            // 2. Se chegou aqui, a autenticação foi bem-sucedida
            // Busca o usuário completo do banco de dados
            var usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

            // 3. Gera o token JWT
            var jwtToken = jwtService.generateToken(usuario);

            // 4. Retorna a resposta
            return AuthResponse.builder()
                    .accessToken(jwtToken)
                    .tokenType("Bearer")
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .role(usuario.getRole())
                    .mensagem("Login realizado com sucesso")
                    .build();

        } catch (BadCredentialsException e) {
            // Credenciais inválidas (email ou senha incorretos)
            throw new BusinessException("Email ou senha inválidos");
        }
    }

    /**
     * Valida se um token JWT é válido.
     *
     * @param token Token JWT a ser validado
     * @return true se o token é válido, false caso contrário
     */
    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            var usuario = usuarioRepository.findByEmail(username)
                    .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
            return jwtService.isTokenValid(token, usuario);
        } catch (Exception e) {
            return false;
        }
    }
}
