package io.github.samuelmonsalvesmoreira.clientes.repository;

import io.github.samuelmonsalvesmoreira.clientes.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
}
