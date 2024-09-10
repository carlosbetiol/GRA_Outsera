# API Golden Raspberry Awards






## Desenvolvedor

[<img src="https://avatars.githubusercontent.com/carlosbetiol" width=115><br><sub>Carlos Betiol</sub>](https://github.com/carlosbetiol)

Linkedin: https://www.linkedin.com/in/carlosbetiol/

Email: carlos@betiol.dev



Objetivo: Desenvolvimento de API de Premiações de Filmes

Documento de especificação: [](https://github.com/carlosbetiol/GRA_Outsera/tree/main/Especificação Backend.pdf)

Github Project: [](https://github.com/users/carlosbetiol/projects/1)




> Status do Projeto: Concluido :heavy_check_mark:



## Tópicos

* [Escopo](#Escopo)

* [Dados técnicos](#Dados-técnicos)

* [Banco de dados](#Banco-de-dados)

* [Execução dos testes de integração](#Execução-dos-testes-de-integração)

* [Compilação e execução via docker container](#Compilação-e-execução-via-docker-container)

* [Compilação e execução direta](#Compilação-e-execução-direta)

* [Documentação da API](#Documentação-da-API)

* [Utilização via Postman](#Utilização-via-Postman)

* [Instruções de uso com OAUTH2 ativado](#Instruções-de-uso-com-OAUTH2 ativado)

* [Respostas](#respostas)

* [Exemplo de resposta](#Exemplo-de-resposta)

* [Referências utilizadas](#Referências-utilizadas)

  

## Escopo

- [x] Diagrama de Entidades e Relacionamentos [](https://github.com/carlosbetiol/GRA_Outsera/tree/main/database-model.pdf)
- [x] Migrations com Flyway
- [x] Filtros com Criteria Filters
- [x] Logs de persistência
- [x] Logs de HTTP Requests (filters and interceptors)
- [x] OpenAPI, SpringDoc e Swagger (http://localhost:8080/docs)
- [x] Internationalization i18n
- [x] Exception Handler Control Advice
- [x] Spring Boot
- [x] Spring Security
- [x] Spring Security Oauth2 Authorization Server
- [x] Customs repositories com Criteria Builder
- [x] Importação de arquivo CSV
- [x] Testes de integração
- [x] Containerização
- [ ] Cache de persistencia
- [ ] Http Cache
- [ ] CI/CD Github Actions e AWS ECR (Elastic Container Register)
- [ ] Deploy on AWS ECS (Elastic Container Service)



## Dados técnicos

Este projeto foi desenvolvido utilizando a IDE IntelliJ e codificado segundo técnicas da modelagem DDD de construções de aplicações.

Foi utilizado o JAVA 17, Spring Boot 3.3.3 e Oauth2 Authorization Server 1.3.2 utilizando o fluxo Authorization Code com PKCE para autenticação.

Maven foi o gerenciador de pacotes utilizado para o projeto na versao 3.9.6.

Todos os parâmetros como: dados de conexão com o banco de dados, porta a ser utilizada, whitelist de endereços de IP, dentre outros, deverão ser enviados como variáveis de ambiente.



## Banco de dados

Utilizado o H2 em memória e populado de acordo com arquivo CSV que deve ser incluido com o nome **movielist.csv** na pasta **src/main/resources/data**.

Estrutura do banco de dados: [](https://github.com/carlosbetiol/GRA_Outsera/tree/main/database-model.pdf)



## Execução dos testes de integração

Na pasta do projeto (**dentro da GoldenRaspberryAwards**), usar o comando abaixo:

```
mvn test -P test,ignore_cors
```



## Compilação e execução via docker container

Na pasta do projeto (**dentro da GoldenRaspberryAwards**) , utilizar para compilação e geração do artefato:



- Para rodar em linux ou windows:

```
mvn package -Pdocker -DskipTests
```



- Para rodar em macOS:

```
mvn package -Pdockermac -DskipTests
```



- Subir o container via docker compose, que ficará respondendo na porta 8080, **com perfil de autenticação desativado**.

```
SPRING_PROFILES_ACTIVE=dev,ignore_cors docker compose up -d
```



- Subir o container via docker compose, que ficará respondendo na porta 8080, **com perfil de autenticação ATIVADO**.

```
SPRING_PROFILES_ACTIVE=prod,ignore_cors docker compose up -d
```



## Compilação e execução direta

Na pasta do projeto (**dentro da GoldenRaspberryAwards**) , utilizar para compilação, geração do artefato e execução.



- Compilar e gerar o pacote JAR:

```
mvn clean package
```



- Subir a aplicação, que ficará respondendo na porta 8080, **com perfil de autenticação desativado**.

```
mvn exec:java -Dspring.profiles.active=dev,ignore_cors
```



- Subir a aplicação, que ficará respondendo na porta 8080, **com perfil de autenticação ATIVADO**.

```
mvn exec:java -Dspring.profiles.active=prod,ignore_cors
```



## Documentação da API

A documentação dos endpoints com suas estruturas de request e responses estão no swagger configurado para a aplicação e pode ser acessado em:

-  http://localhost:8080/docs - caso a API esteja em execução em modo PROD, será exigida autenticação Oauth2 para execução dos endpoints e autenticação basic para abrir em modo de leitura.

Abaixo as credenciais para modo de leitura caso a autenticação seja necessária:

**login: admin@gra.com**

**senha: Enc$738**



## Utilização via Postman

Para testes via Postman, pode ser feito o import do json abaixo:

- https://github.com/carlosbetiol/GRA_Outsera/tree/main/GRA.postman_collection.json



## Instruções de uso com OAUTH2 ativado

to do: write the content






------

[<img src="https://avatars.githubusercontent.com/carlosbetiol" width=115><br><sub>Carlos Betiol</sub>](https://github.com/carlosbetiol)

Linkedin: https://www.linkedin.com/in/carlosbetiol/

Email: carlos@betiol.dev
