package io.github.samuelmonsalvesmoreira.clientes.exception;

import java.time.Instant;

public record RespostaErro(
        Instant instante,
        int status,
        String erro,
        String mensagem,
        String caminho
) {
}
