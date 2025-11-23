package br.edu.ifrn.bancosangue.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 * Utiliza a biblioteca JJWT para manipulação de tokens.
 */
@Service
public class JwtService {

    private final String secretKey;
    private final long jwtExpiration;

    /**
     * Construtor com injeção de dependências das propriedades JWT.
     *
     * @param secretKey Chave secreta para assinar os tokens (mínimo 256 bits)
     * @param jwtExpiration Tempo de expiração do token em milissegundos
     */
    public JwtService(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration}") long jwtExpiration
    ) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
    }

    /**
     * Extrai o username (subject) do token JWT.
     *
     * @param token Token JWT
     * @return Username extraído do token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai um claim específico do token JWT.
     *
     * @param token Token JWT
     * @param claimsResolver Função para extrair o claim desejado
     * @param <T> Tipo do claim
     * @return Claim extraído
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Gera um token JWT para o usuário.
     *
     * @param userDetails Detalhes do usuário
     * @return Token JWT gerado
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Gera um token JWT com claims adicionais.
     *
     * @param extraClaims Claims adicionais a serem incluídos no token
     * @param userDetails Detalhes do usuário
     * @return Token JWT gerado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Constrói o token JWT.
     *
     * @param extraClaims Claims adicionais
     * @param userDetails Detalhes do usuário
     * @param expiration Tempo de expiração
     * @return Token JWT construído
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .claims(extraClaims) // Claims personalizados
                .subject(userDetails.getUsername()) // Subject (username/email)
                .issuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Data de expiração
                .signWith(getSignInKey()) // Assinatura com chave secreta
                .compact();
    }

    /**
     * Valida se o token JWT é válido para o usuário.
     *
     * @param token Token JWT
     * @param userDetails Detalhes do usuário
     * @return true se o token é válido, false caso contrário
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Verifica se o token JWT está expirado.
     *
     * @param token Token JWT
     * @return true se o token está expirado, false caso contrário
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração do token JWT.
     *
     * @param token Token JWT
     * @return Data de expiração
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai todos os claims do token JWT.
     *
     * @param token Token JWT
     * @return Todos os claims do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Verifica com a chave secreta
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtém a chave de assinatura a partir da secret key.
     * Converte a string secret key em um SecretKey usando HMAC-SHA.
     *
     * @return Chave de assinatura
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
