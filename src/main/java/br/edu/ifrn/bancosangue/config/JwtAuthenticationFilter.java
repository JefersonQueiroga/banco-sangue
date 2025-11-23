package br.edu.ifrn.bancosangue.config;

import br.edu.ifrn.bancosangue.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Como estamos usando JWT precisa desse filtro para validar o token.
 * Filtro de autenticação JWT que intercepta todas as requisições HTTP.
 * */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Método principal do filtro que processa cada requisição.
     *
     * Fluxo de execução:
     * 1. Extrai o token JWT do header Authorization
     * 2. Se o token existir, extrai o username (email)
     * 3. Se o usuário não estiver autenticado, carrega os detalhes do usuário
     * 4. Valida o token
     * 5. Se válido, configura a autenticação no SecurityContext
     * 6. Passa a requisição para o próximo filtro na cadeia
     *
     * @param request Requisição HTTP
     * @param response Resposta HTTP
     * @param filterChain Cadeia de filtros
     * @throws ServletException se ocorrer erro no servlet
     * @throws IOException se ocorrer erro de I/O
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extrai o header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Verifica se o header existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Se não houver token, continua a cadeia de filtros sem autenticar
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token (remove "Bearer " do início)
        jwt = authHeader.substring(7);

        try {
            // 4. Extrai o username (email) do token
            userEmail = jwtService.extractUsername(jwt);

            // 5. Verifica se o email foi extraído e se o usuário não está autenticado
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6. Carrega os detalhes do usuário do banco de dados
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // 7. Valida o token
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    // 8. Cria o objeto de autenticação do Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Credentials (não precisamos enviar a senha aqui)
                            userDetails.getAuthorities() // Permissões do usuário
                    );

                    // 9. Adiciona detalhes da requisição ao token de autenticação
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 10. Configura a autenticação no SecurityContext
                    // A partir daqui, o usuário está autenticado para esta requisição
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Em caso de erro (token inválido, expirado, etc.), não autentica
            // O erro será tratado pelo AuthenticationEntryPoint configurado
            logger.error("Erro ao processar token JWT: " + e.getMessage());
        }

        // 11. Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
