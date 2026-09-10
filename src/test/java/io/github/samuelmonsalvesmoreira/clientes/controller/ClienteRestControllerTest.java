package io.github.samuelmonsalvesmoreira.clientes.controller;

import io.github.samuelmonsalvesmoreira.clientes.exception.ClienteNaoEncontradoException;
import io.github.samuelmonsalvesmoreira.clientes.exception.GlobalExceptionHandler;
import io.github.samuelmonsalvesmoreira.clientes.model.Cliente;
import io.github.samuelmonsalvesmoreira.clientes.model.Endereco;
import io.github.samuelmonsalvesmoreira.clientes.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class ClienteRestControllerTest {

    private ClienteService clienteService;
    private MockMvc mockMvc;

    @BeforeEach
    void preparar() {
        clienteService = mock(ClienteService.class);
        ClienteRestController controller = new ClienteRestController(clienteService);
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveListarClientes() throws Exception {
        when(clienteService.buscarTodos()).thenReturn(List.of(criarCliente()));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Samuel"));
    }

    @Test
    void deveRetornarCriadoAoInserirClienteValido() throws Exception {
        when(clienteService.inserir(any(Cliente.class))).thenAnswer(chamada -> chamada.getArgument(0));

        String json = """
                {
                  "nome": "Samuel",
                  "endereco": {
                    "cep": "01001-000"
                  }
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Samuel"));
    }

    @Test
    void deveRetornarErroQuandoDadosForemInvalidos() throws Exception {
        String json = """
                {
                  "nome": "",
                  "endereco": {
                    "cep": "123"
                  }
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(clienteService, never()).inserir(any());
    }

    @Test
    void deveRetornarNaoEncontradoQuandoIdNaoExistir() throws Exception {
        when(clienteService.buscarPorId(99L)).thenThrow(new ClienteNaoEncontradoException(99L));

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornarSemConteudoAoExcluir() throws Exception {
        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService).deletar(1L);
    }

    private Cliente criarCliente() {
        Endereco endereco = new Endereco();
        endereco.setCep("01001000");

        Cliente cliente = new Cliente();
        cliente.setNome("Samuel");
        cliente.setEndereco(endereco);
        return cliente;
    }
}
