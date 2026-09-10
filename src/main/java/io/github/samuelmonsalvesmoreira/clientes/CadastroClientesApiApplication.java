package io.github.samuelmonsalvesmoreira.clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CadastroClientesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroClientesApiApplication.class, args);
	}

}
