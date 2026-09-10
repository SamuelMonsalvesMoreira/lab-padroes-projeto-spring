package io.github.samuelmonsalvesmoreira.clientes.service.impl;

import io.github.samuelmonsalvesmoreira.clientes.exception.CepInvalidoException;
import io.github.samuelmonsalvesmoreira.clientes.exception.ClienteNaoEncontradoException;
import io.github.samuelmonsalvesmoreira.clientes.model.Cliente;
import io.github.samuelmonsalvesmoreira.clientes.model.Endereco;
import io.github.samuelmonsalvesmoreira.clientes.repository.ClienteRepository;
import io.github.samuelmonsalvesmoreira.clientes.repository.EnderecoRepository;
import io.github.samuelmonsalvesmoreira.clientes.service.ClienteService;
import io.github.samuelmonsalvesmoreira.clientes.service.ViaCepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final ViaCepService viaCepService;

    public ClienteServiceImpl(
            ClienteRepository clienteRepository,
            EnderecoRepository enderecoRepository,
            ViaCepService viaCepService
    ) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.viaCepService = viaCepService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return procurarCliente(id);
    }

    @Override
    @Transactional
    public Cliente inserir(Cliente cliente) {
        cliente.setEndereco(buscarEndereco(cliente));
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = procurarCliente(id);
        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEndereco(buscarEndereco(clienteAtualizado));
        return clienteRepository.save(clienteExistente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Cliente cliente = procurarCliente(id);
        clienteRepository.delete(cliente);
    }

    private Cliente procurarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    private Endereco buscarEndereco(Cliente cliente) {
        if (cliente == null || cliente.getEndereco() == null) {
            throw new CepInvalidoException("Informe o endereço e o CEP do cliente.");
        }

        String cep = normalizarCep(cliente.getEndereco().getCep());
        return enderecoRepository.findById(cep)
                .orElseGet(() -> consultarESalvarEndereco(cep));
    }

    private Endereco consultarESalvarEndereco(String cep) {
        Endereco endereco = viaCepService.consultarCep(cep);
        if (endereco == null || Boolean.TRUE.equals(endereco.getErro())) {
            throw new CepInvalidoException("CEP não encontrado.");
        }

        endereco.setCep(cep);
        return enderecoRepository.save(endereco);
    }

    private String normalizarCep(String cepInformado) {
        if (cepInformado == null || cepInformado.isBlank()) {
            throw new CepInvalidoException("Informe o CEP do cliente.");
        }

        String cep = cepInformado.replaceAll("\\D", "");
        if (cep.length() != 8) {
            throw new CepInvalidoException("O CEP deve ter 8 números.");
        }
        return cep;
    }
}
