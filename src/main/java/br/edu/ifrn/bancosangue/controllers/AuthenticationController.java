package br.edu.ifrn.bancosangue.controllers;

import br.edu.ifrn.bancosangue.controllers.docs.AuthenticationApiDoc;
import br.edu.ifrn.bancosangue.dtos.AuthResponse;
import br.edu.ifrn.bancosangue.dtos.LoginRequest;
import br.edu.ifrn.bancosangue.dtos.RegisterRequest;
import br.edu.ifrn.bancosangue.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para endpoints de autenticação.
 * Fornece endpoints para login e registro de usuários.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApiDoc {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint para registro de novo usuário.
     *
     * @param request Dados de registro do usuário
     * @return AuthResponse com token JWT e dados do usuário
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para login (autenticação) de usuário.
     *
     * @param request Credenciais de login (email e senha)
     * @return AuthResponse com token JWT e dados do usuário
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}
