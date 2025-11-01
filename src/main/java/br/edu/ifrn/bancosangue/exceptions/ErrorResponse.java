package br.edu.ifrn.bancosangue.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String mensagem;
    private Map<String, String> erros;
    private LocalDateTime timestamp;
}