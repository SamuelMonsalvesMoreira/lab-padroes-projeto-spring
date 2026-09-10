package io.github.samuelmonsalvesmoreira.clientes.repository;

import io.github.samuelmonsalvesmoreira.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
