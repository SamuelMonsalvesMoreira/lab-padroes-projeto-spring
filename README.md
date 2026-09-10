# Cadastro de Clientes API

API REST para cadastrar clientes e preencher automaticamente o endereço a partir do CEP informado.

O projeto foi desenvolvido para o desafio **Explorando Padrões de Projetos na Prática com Java**, da Digital Innovation One. A ideia original do professor foi mantida e adaptada para versões atuais do Java e do Spring Boot.

## Como a aplicação funciona

```text
Cliente envia nome e CEP
          ↓
Controller recebe a requisição
          ↓
Service aplica as regras
          ↓
ViaCEP fornece o endereço
          ↓
Repositories salvam os dados no H2
```

## Funcionalidades

- Cadastrar um cliente informando nome e CEP
- Consultar todos os clientes
- Consultar um cliente pelo ID
- Atualizar um cliente
- Excluir um cliente
- Consultar automaticamente o endereço pelo ViaCEP
- Reutilizar endereços que já estão salvos no banco
- Validar nome, endereço e CEP
- Retornar respostas de erro claras

## Tecnologias

- Java 26
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Spring Cloud OpenFeign
- Jakarta Validation
- H2 Database
- Maven
- JUnit e Mockito

## Endpoints

| Método | Endpoint | Função | Resposta de sucesso |
|---|---|---|---|
| `GET` | `/clientes` | Lista todos os clientes | `200 OK` |
| `GET` | `/clientes/{id}` | Busca um cliente pelo ID | `200 OK` |
| `POST` | `/clientes` | Cadastra um cliente | `201 Created` |
| `PUT` | `/clientes/{id}` | Atualiza um cliente | `200 OK` |
| `DELETE` | `/clientes/{id}` | Exclui um cliente | `204 No Content` |

### Exemplo de cadastro

Requisição para `POST /clientes`:

```json
{
  "nome": "Samuel",
  "endereco": {
    "cep": "01001-000"
  }
}
```

Resposta:

```json
{
  "id": 1,
  "nome": "Samuel",
  "endereco": {
    "cep": "01001000",
    "logradouro": "Praça da Sé",
    "complemento": "lado ímpar",
    "bairro": "Sé",
    "localidade": "São Paulo",
    "uf": "SP",
    "estado": "São Paulo",
    "regiao": "Sudeste",
    "ddd": "11"
  }
}
```

> Para cadastrar ou atualizar um CEP que ainda não está salvo, a aplicação precisa de acesso à internet para consultar o ViaCEP.

## Respostas de erro

- `400 Bad Request`: nome, endereço ou CEP inválido
- `404 Not Found`: cliente não encontrado
- `502 Bad Gateway`: não foi possível consultar o ViaCEP

Exemplo:

```json
{
  "instante": "2026-09-10T13:35:02Z",
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Cliente não encontrado com o ID 1.",
  "caminho": "/clientes/1"
}
```

## Padrões de projeto aplicados

### Repository

As interfaces `ClienteRepository` e `EnderecoRepository` separam o acesso ao banco das regras da aplicação.

### Strategy

A interface `ClienteService` define as operações disponíveis. A classe `ClienteServiceImpl` contém a implementação dessas operações.

### Facade

O serviço de clientes oferece uma entrada simples para coordenar os repositórios e a consulta externa ao ViaCEP.

### Singleton

Por padrão, o Spring cria uma única instância dos componentes administrados, como services, repositories e controllers, e reutiliza essas instâncias durante a execução.

## Organização do código

```text
src
├── main
│   ├── java/io/github/samuelmonsalvesmoreira/clientes
│   │   ├── controller
│   │   ├── exception
│   │   ├── model
│   │   ├── repository
│   │   └── service
│   └── resources
│       └── application.properties
└── test
    └── java/io/github/samuelmonsalvesmoreira/clientes
        ├── controller
        └── service
```

| Pacote | Responsabilidade |
|---|---|
| `model` | Representar clientes e endereços |
| `repository` | Salvar e consultar dados no banco |
| `service` | Aplicar regras e coordenar as operações |
| `controller` | Receber as requisições HTTP |
| `exception` | Padronizar o tratamento de erros |

## Como executar pelo IntelliJ IDEA

1. Abra o arquivo `pom.xml` como projeto.
2. Aguarde o Maven carregar as dependências.
3. Verifique se o projeto está usando o JDK 26.
4. Abra `CadastroClientesApiApplication.java`.
5. Clique no botão verde ao lado do método `main`.
6. Confirme no console que o Tomcat iniciou na porta `8080`.
7. Teste os endpoints em `http://localhost:8080/clientes` usando Postman, Insomnia ou outro cliente HTTP.

## Banco de dados H2

O banco funciona em memória e seus dados são apagados quando a aplicação é encerrada.

- Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:clientes`
- Usuário: `sa`
- Senha: deixe em branco

## Testes automatizados

Os testes cobrem as regras do serviço, a reutilização de endereços, a consulta ao ViaCEP, as validações e as respostas dos endpoints.

No IntelliJ, clique com o botão direito sobre `src/test` e escolha **Run 'All Tests'**.

## Referências

- [Projeto de referência da DIO](https://github.com/digitalinnovationone/lab-padroes-projeto-spring)
- [ViaCEP](https://viacep.com.br/)
- [Spring Initializr](https://start.spring.io/)

## Autor

Desenvolvido por **Samuel Monsalves Moreira** como projeto de estudo de Java, Spring Boot e padrões de projeto.
