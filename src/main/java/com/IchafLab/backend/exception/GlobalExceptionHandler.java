package com.IchafLab.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Produto não encontrado: 404.
    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail tratarNaoEncontrado(NoSuchElementException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Dados inválidos lançados pelo service: 400.
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail tratarDadosInvalidos(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Falha nas anotações de validação (@NotBlank, @Positive etc.) do @Valid: 400.
    // Devolve a lista de campos com problema e a mensagem de cada um.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erros.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Dados inválidos. Verifique os campos informados.");
        problema.setProperty("erros", erros);
        return problema;
    }
}