package io.github.samuelmonsalvesmoreira.clientes.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<RespostaErro> tratarClienteNaoEncontrado(
            ClienteNaoEncontradoException erro,
            HttpServletRequest requisicao
    ) {
        return criarResposta(HttpStatus.NOT_FOUND, erro.getMessage(), requisicao.getRequestURI());
    }

    @ExceptionHandler(CepInvalidoException.class)
    public ResponseEntity<RespostaErro> tratarCepInvalido(
            CepInvalidoException erro,
            HttpServletRequest requisicao
    ) {
        return criarResposta(HttpStatus.BAD_REQUEST, erro.getMessage(), requisicao.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErro> tratarValidacao(
            MethodArgumentNotValidException erro,
            HttpServletRequest requisicao
    ) {
        String mensagem = erro.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));

        return criarResposta(HttpStatus.BAD_REQUEST, mensagem, requisicao.getRequestURI());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<RespostaErro> tratarFalhaNoViaCep(
            FeignException erro,
            HttpServletRequest requisicao
    ) {
        return criarResposta(
                HttpStatus.BAD_GATEWAY,
                "Não foi possível consultar o ViaCEP no momento.",
                requisicao.getRequestURI()
        );
    }

    private ResponseEntity<RespostaErro> criarResposta(
            HttpStatus status,
            String mensagem,
            String caminho
    ) {
        RespostaErro resposta = new RespostaErro(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                caminho
        );
        return ResponseEntity.status(status).body(resposta);
    }
}
