# Sistema de Gestão de Tabaco

Este é um sistema de gestão para produtores de tabaco, utilizando Spring Boot e PostgreSQL.

## Requisitos

- Docker
- Docker Compose

## Como Executar a Aplicação

1. Clone este repositório para sua máquina local:
   ```
   git clone [url-do-repositorio]
   cd [nome-da-pasta]
   ```

2. Torne os scripts executáveis:
   ```
   chmod +x scripts/start.sh scripts/stop.sh
   ```

3. Inicie a aplicação:
   ```
   ./scripts/start.sh
   ```

4. Para parar a aplicação:
   ```
   ./scripts/stop.sh
   ```

## Portas e Acessos

- **Aplicação Spring Boot**: http://localhost:8080
- **Banco de Dados PostgreSQL**: 
  - Host: localhost
  - Porta: 5432
  - Usuário: postgres
  - Senha: 111111
  - Schema: TOBACCO

## Documentação da API

A aplicação possui documentação Swagger integrada, que pode ser acessada em:

```
http://localhost:8080/swagger-ui
```

Através da interface do Swagger, você pode visualizar, testar e entender todos os endpoints disponíveis na API.

Para facilitar os testes e validação da ferramenta, abaixo deixo disponível a Collection do Postamn utilizada para testes e desenvolvimento.

[Postman Collection](UTC%20API.postman_collection.json)

## Estrutura do Banco de Dados

O banco de dados contém as seguintes tabelas no schema TOBACCO:

- **Producers**: Cadastro de produtores
- **Classes**: Classes de tabaco
- **Bundles**: Fardos de tabaco
- **Transactions**: Transações de compra

## Solução de Problemas

Se encontrar problemas com portas já alocadas, edite o arquivo `docker-compose.yml` para alterar as portas usadas pelos containers.
