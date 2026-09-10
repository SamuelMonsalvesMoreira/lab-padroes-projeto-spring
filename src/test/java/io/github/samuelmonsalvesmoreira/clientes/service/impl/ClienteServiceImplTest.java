package io.github.samuelmonsalvesmoreira.clientes.service.impl;

import io.github.samuelmonsalvesmoreira.clientes.exception.CepInvalidoException;
import io.github.samuelmonsalvesmoreira.clientes.exception.ClienteNaoEncontradoException;
import io.github.samuelmonsalvesmoreira.clientes.model.Cliente;
import io.github.samuelmonsalvesmoreira.clientes.model.Endereco;
import io.github.samuelmonsalvesmoreira.clientes.repository.ClienteRepository;
import io.github.samuelmonsalvesmoreira.clientes.repository.EnderecoRepository;
import io.github.samuelmonsalvesmoreira.clientes.service.ViaCepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ViaCepService viaCepService;

    private ClienteServiceImpl clienteService;

    @BeforeEach
    void preparar() {
        clienteService = new ClienteServiceImpl(clienteRepository, enderecoRepository, viaCepService);
    }

    @Test
    void deveConsultarViaCepAoInserirEnderecoNovo() {
        Cliente cliente = criarCliente("Samuel", "01001-000");
        Endereco enderecoViaCep = criarEndereco("01001000");

        when(enderecoRepository.findById("01001000")).thenReturn(Optional.empty());
        when(viaCepService.consultarCep("01001000")).thenReturn(enderecoViaCep);
        when(enderecoRepository.save(enderecoViaCep)).thenReturn(enderecoViaCep);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.inserir(cliente);

        assertSame(cliente, resultado);
        assertSame(enderecoViaCep, resultado.getEndereco());
        assertEquals("01001000", resultado.getEndereco().getCep());
        verify(viaCepService).consultarCep("01001000");
        verify(enderecoRepository).save(enderecoViaCep);
    }

    @Test
    void deveReutilizarEnderecoQueJaEstaNoBanco() {
        Cliente cliente = criarCliente("Samuel", "01001000");
        Endereco enderecoSalvo = criarEndereco("01001000");

        when(enderecoRepository.findById("01001000")).thenReturn(Optional.of(enderecoSalvo));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        clienteService.inserir(cliente);

        assertSame(enderecoSalvo, cliente.getEndereco());
        verify(viaCepService, never()).consultarCep(any());
        verify(enderecoRepository, never()).save(any());
    }

    @Test
    void deveRejeitarCepComQuantidadeIncorreta() {
        Cliente cliente = criarCliente("Samuel", "123");

        CepInvalidoException erro = assertThrows(
                CepInvalidoException.class,
                () -> clienteService.inserir(cliente)
        );

        assertEquals("O CEP deve ter 8 números.", erro.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveRejeitarCepNaoEncontradoPeloViaCep() {
        Cliente cliente = criarCliente("Samuel", "00000000");
        Endereco respostaViaCep = new Endereco();
        respostaViaCep.setErro(true);

        when(enderecoRepository.findById("00000000")).thenReturn(Optional.empty());
        when(viaCepService.consultarCep("00000000")).thenReturn(respostaViaCep);

        assertThrows(CepInvalidoException.class, () -> clienteService.inserir(cliente));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveAtualizarClienteExistente() {
        Cliente clienteExistente = criarCliente("Nome antigo", "01001000");
        Cliente novosDados = criarCliente("Nome novo", "30140071");
        Endereco novoEndereco = criarEndereco("30140071");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(enderecoRepository.findById("30140071")).thenReturn(Optional.of(novoEndereco));
        when(clienteRepository.save(clienteExistente)).thenReturn(clienteExistente);

        Cliente resultado = clienteService.atualizar(1L, novosDados);

        assertSame(clienteExistente, resultado);
        assertEquals("Nome novo", resultado.getNome());
        assertSame(novoEndereco, resultado.getEndereco());
    }

    @Test
    void deveInformarQuandoClienteNaoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.buscarPorId(99L));
    }

    private Cliente criarCliente(String nome, String cep) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEndereco(criarEndereco(cep));
        return cliente;
    }

    private Endereco criarEndereco(String cep) {
        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        endereco.setLogradouro("Praça da Sé");
        endereco.setLocalidade("São Paulo");
        endereco.setUf("SP");
        return endereco;
    }
}
