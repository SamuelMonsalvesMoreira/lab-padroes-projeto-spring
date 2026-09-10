# Cadastro de Clientes API

API REST em desenvolvimento para cadastrar clientes e preencher seus endereços a partir do CEP.

O projeto faz parte do desafio **Explorando Padrões de Projetos na Prática com Java**, da Digital Innovation One. A implementação usa uma versão atualizada da plataforma Java e do Spring Boot.

## Objetivo

Praticar a organização de uma aplicação Spring e entender como padrões de projeto aparecem em uma situação real.

Quando estiver concluída, a aplicação seguirá este fluxo:

```text
Usuário informa nome e CEP
           ↓
API recebe os dados
           ↓
ViaCEP fornece o endereço
           ↓
Cliente e endereço são salvos no banco
```

## Situação do projeto

O projeto está sendo construído por etapas.

- [x] Criação do projeto com Spring Initializr
- [x] Configuração das dependências
- [x] Criação dos modelos `Cliente` e `Endereco`
- [ ] Criação dos repositórios
- [ ] Integração com a API ViaCEP
- [ ] Implementação das regras no serviço
- [ ] Criação dos endpoints REST
- [ ] Tratamento de erros
- [ ] Testes automatizados
- [ ] Documentação dos endpoints

## Funcionalidades planejadas

- Cadastrar um cliente informando nome e CEP
- Consultar todos os clientes
- Consultar um cliente pelo ID
- Atualizar um cliente
- Excluir um cliente
- Consultar automaticamente o endereço pelo CEP
- Validar os dados enviados

> Os endpoints ainda não foram implementados. Esta seção descreve o resultado esperado ao final do desafio.

## Tecnologias

- Java 26
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Spring Cloud OpenFeign
- Jakarta Validation
- H2 Database
- Maven

## Padrões de projeto estudados

### Repository

Separa o acesso ao banco de dados das demais regras da aplicação. Será utilizado pelos repositórios de clientes e endereços.

### Strategy

Uma interface define as operações de clientes, enquanto uma classe de serviço fornece a implementação dessas operações.

### Facade

O serviço de clientes oferecerá uma entrada simples para coordenar o banco de dados e a consulta externa ao ViaCEP.

### Singleton

Por padrão, o Spring cria uma única instância dos componentes administrados, como serviços e controllers, e reutiliza essa instância na aplicação.

## Organização do código

```text
src/main/java/io/github/samuelmonsalvesmoreira/clientes
├── CadastroClientesApiApplication.java
├── controller
├── model
│   ├── Cliente.java
│   └── Endereco.java
├── repository
└── service
```

| Pacote | Responsabilidade |
|---|---|
| `model` | Representar os dados de clientes e endereços |
| `repository` | Salvar e consultar informações no banco |
| `service` | Aplicar regras e coordenar as operações |
| `controller` | Receber e responder às requisições HTTP |

## Como executar pelo IntelliJ IDEA

1. Abra o arquivo `pom.xml` como projeto.
2. Aguarde o carregamento das dependências do Maven.
3. Verifique se o projeto está utilizando o JDK 26.
4. Abra `CadastroClientesApiApplication.java`.
5. Execute o método `main` pelo botão verde.
6. Confirme no console a mensagem de que o Tomcat iniciou na porta `8080`.

Enquanto nenhum endpoint estiver criado, acessar `http://localhost:8080` poderá retornar uma página 404. Isso é esperado.

## Banco de dados

Durante o desenvolvimento será utilizado o H2, um banco em memória. Os dados podem ser perdidos quando a aplicação é encerrada, o que é adequado para os primeiros testes.

## Referência

- [Desafio de padrões de projeto da DIO](https://github.com/digitalinnovationone/lab-padroes-projeto-spring)
- [ViaCEP](https://viacep.com.br/)
- [Spring Initializr](https://start.spring.io/)

## Autor

Desenvolvido por **Samuel Monsalves Moreira** como projeto de estudo de Java, Spring Boot e padrões de projeto.

