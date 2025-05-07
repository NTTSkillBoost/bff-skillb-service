package br.com.nttdata.skilbost.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
//RFC 9457/RFC 7807 — Problem Details for HTTP APIs mais avançado
//RFC 7807 — Problem Details for HTTP APIs.
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ProblemDetail> handleWebClientResponseException(WebClientResponseException ex, ServerWebExchange exchange) {

        // Corpo do erro que veio do serviço downstream
        String downstreamBody = ex.getResponseBodyAsString();
        // Tenta transformar o corpo em JSON. Se não conseguir, mantém como string.
        Object downstreamResponse = convertDownstreamBody(downstreamBody);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                ex.getStatusCode(),
                ex.getMessage()
        );
        problem.setTitle("Erro ao chamar serviço downstream");
        problem.setInstance(URI.create(exchange.getRequest().getPath().toString()));
        problem.setType(URI.create("https://example.com/errors/downstream"));

        // Inclui o corpo original do erro como propriedade extra
        problem.setProperty("downstreamResponse", downstreamResponse);

        return ResponseEntity.status(ex.getStatusCode()).body(problem);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, ServerWebExchange exchange) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                ex.getMessage()
        );
        problem.setTitle("Erro inesperado");
        problem.setInstance(URI.create(exchange.getRequest().getPath().toString()));
        problem.setType(URI.create("https://example.com/errors/unexpected"));

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problem);
    }

    private Object convertDownstreamBody(String downstreamBody) {
        try {
            return objectMapper.readTree(downstreamBody);
        } catch (Exception e) {
            return downstreamBody; // Se não for JSON, retorna como texto mesmo.
        }
    }
}