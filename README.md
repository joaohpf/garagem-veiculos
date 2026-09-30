# Garagem de Veículos

Sistema de gerenciamento de garagem desenvolvido para a disciplina **Arquitetura de Software** da **UniRV**.

## Integrantes

- João Henrique
- Gustavo Lopes
- João Victor

## Linguagem e Framework

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3.3 + Thymeleaf
- **Persistência:** Arquivos JSON (Jackson)

## Como executar

### Pré-requisitos

- JDK 17+
- Maven 3.8+


## Arquitetura

```
MVC + Repository + Injeção de Dependência (SOLID)

View → Controller → IPessoaRepository (interface) → PessoaRepository → pessoas.json
```

Cada entidade segue a estrutura:

| Papel              | Arquivo                              |
|--------------------|--------------------------------------|
| Model              | `model/Pessoa.java`                  |
| Interface          | `repository/IPessoaRepository.java`  |
| Repositório (JSON) | `repository/PessoaRepository.java`   |
| Controller         | `controller/PessoaController.java`   |
| View listagem      | `templates/pessoa/index.html`        |
| View formulário    | `templates/pessoa/PessoaForm.html`   |

## Ferramentas de IA utilizadas

- **Claude** (Anthropic) — geração e revisão do código
