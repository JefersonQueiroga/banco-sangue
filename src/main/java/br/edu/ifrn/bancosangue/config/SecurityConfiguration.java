package br.edu.ifrn.bancosangue.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração de segurança do Spring Security.
 * Define as regras de autenticação, autorização e proteção da aplicação.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilita anotações de segurança em métodos (@PreAuthorize, @Secured, etc.)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Configura a cadeia de filtros de segurança (SecurityFilterChain).
     * Define quais endpoints são públicos e quais requerem autenticação.
     *
     * @param http HttpSecurity para configuração
     * @return SecurityFilterChain configurado
     * @throws Exception se ocorrer erro na configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF (Cross-Site Request Forgery) pois estamos usando JWT
                // JWT é stateless e não usa cookies, então CSRF não é necessário
                .csrf(AbstractHttpConfigurer::disable)

                // Configura CORS (Cross-Origin Resource Sharing)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configura autorização de requisições
                .authorizeHttpRequests(auth -> auth
                        // ========== Aqui liberas os endpoints publicos ==========

                        // Endpoints de autenticação
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/auth/login",
                                "/api/auth/register"
                        ).permitAll()

                        // Documentação Swagger/OpenAPI
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // Actuator (Health Check)
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()

                        // Consulta pública de doadores (apenas GET)
                        .requestMatchers(HttpMethod.GET, "/api/doadores/**").permitAll()

                        // ========== Endpoints Protegidos (requerem autenticação) ==========

                        // Todos os outros endpoints requerem autenticação
                        .anyRequest().authenticated()
                )

                // Configura gerenciamento de sessão como STATELESS
                // Não cria nem usa sessões HTTP, pois estamos usando JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configura o provider de autenticação
                .authenticationProvider(authenticationProvider())

                // Adiciona o filtro JWT antes do filtro de autenticação padrão
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura o provider de autenticação.
     * Usa DaoAuthenticationProvider que busca usuários do banco de dados
     * via UserDetailsService e valida senhas com BCrypt.
     *
     * @return AuthenticationProvider configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configura o AuthenticationManager.
     * Usado para autenticar usuários programaticamente (ex: no endpoint de login).
     *
     * @param config Configuração de autenticação
     * @return AuthenticationManager
     * @throws Exception se ocorrer erro ao obter o manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura o encoder de senhas usando BCrypt.
     * BCrypt é um algoritmo de hash seguro e lento (por design),
     * ideal para armazenar senhas.
     *
     * @return PasswordEncoder configurado com BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura CORS (Cross-Origin Resource Sharing).
     * Permite que o frontend em diferentes origens acesse a API.
     *
     * @return CorsConfigurationSource configurado
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite requisições de qualquer origem (ajustar em produção)
        configuration.setAllowedOrigins(List.of("*"));

        // Permite todos os métodos HTTP
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Permite todos os headers
        configuration.setAllowedHeaders(List.of("*"));

        // Permite envio de credenciais (cookies, authorization headers)
        // ATENÇÃO: Se allowCredentials = true, allowedOrigins não pode ser "*"
        // configuration.setAllowCredentials(true);

        // Expõe o header Authorization nas respostas
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
