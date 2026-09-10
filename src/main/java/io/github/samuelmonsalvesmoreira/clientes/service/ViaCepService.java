package io.github.samuelmonsalvesmoreira.clientes.service;

import io.github.samuelmonsalvesmoreira.clientes.model.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCep", url = "${viacep.url:https://viacep.com.br/ws}")
public interface ViaCepService {

    @GetMapping("/{cep}/json/")
    Endereco consultarCep(@PathVariable("cep") String cep);
}
