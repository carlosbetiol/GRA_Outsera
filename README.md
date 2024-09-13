# API Golden Raspberry Awards






## Desenvolvedor

[<img src="https://avatars.githubusercontent.com/carlosbetiol" width=115><br><sub>Carlos Betiol</sub>](https://github.com/carlosbetiol)

Linkedin: https://www.linkedin.com/in/carlosbetiol/

Email: carlos@betiol.dev



Objetivo: Desenvolvimento de API de Premiações de Filmes com endpoint de **TOP/LEAST PRODUCERS**

Documento de especificação: [Especificacao Backend.pdf](https://github.com/carlosbetiol/GRA_Outsera/tree/main/Especificação Backend.pdf)

Github Project: [Project Roadmap](https://github.com/users/carlosbetiol/projects/1)




> Status do Projeto: Concluido :heavy_check_mark:



## Dados técnicos

Este projeto foi desenvolvido utilizando a IDE IntelliJ e codificado segundo técnicas da modelagem DDD de construções de aplicações.

Foi utilizado o **JAVA 17** e **Spring Boot 3.3.3**.

**Maven** foi o gerenciador de pacotes utilizado para o projeto na **versao 3.9.6.**

Todos os parâmetros como: dados de conexão com o banco de dados, porta a ser utilizada, whitelist de endereços de IP, dentre outros, deverão ser enviados como variáveis de ambiente.



## Banco de dados

Utilizado o H2 em memória e populado de acordo com arquivo CSV que deve ser incluido com o nome **movielist.csv** na pasta **GoldenRaspberryAwards/data**.

Estrutura do banco de dados: [database-model.pdf](https://github.com/carlosbetiol/GRA_Outsera/tree/main/database-model.pdf)



## Execução dos testes de integração

No diretório do projeto (**dentro do subdiretório GoldenRaspberryAwards**), usar o comando abaixo:

> [!WARNING]
>
> Para passar nos testes será necessário manter o **movielist.csv** **ORIGINAL** no diretório **GoldenRaspberryAwards/data**



Primeiro fazer o build do projeto:

```
mvn clean package -DskipTests
```

Em seguida rodar os testes com:

```
mvn test -P test
```



## Compilação e execução direta (Segurança DESATIVADA)

No diretório do projeto (**dentro do subdiretório GoldenRaspberryAwards**) , utilizar para compilação, geração do artefato e execução.



> [!IMPORTANT]
>
> Ao levantar a API, caso seja necessário utilizar outro arquivo **movieslist.csv** que não seja o original para população do banco de dados, ele deverá ser colocado no diretório **GoldenRaspberryAwards/data** mantendo o mesmo nome **movielist.csv**. Isso deve ser feito antes da execução da aplicação, que de forma automática o utilizará para popular as tabelas.



- Compilar e gerar o pacote JAR:

```
mvn clean package -DskipTests
```



- Subir a aplicação, que ficará respondendo **na porta 8080**

```
mvn exec:java -Dspring.profiles.active=dev
```



## Endpoint de Top/Least Producers

Com a aplicação rodando, o endpoint **REQUISITO** é o seguinte:

Método **GET**

**http://localhost:8080/v1/producers/reports/top-least**



## Utilização da API via Postman

Para testes via Postman, pode ser feito o import do json abaixo contendo todos os enpoints da API diretamente no POSTMAN (somente mudar o HOST):

- [GRA.postman collection.json](https://github.com/carlosbetiol/GRA_Outsera/tree/main/GRA.postman_collection.json)



## Utilização da API via Swagger

Os endpoints da API podem ser executados diretamente através da documentação via **Swagger** no seguinte endereço:

**http://localhost:8080/docs**



## Práticas utilizadas

- [x] Diagrama de Entidades e Relacionamentos [database-model.pdf](https://github.com/carlosbetiol/GRA_Outsera/tree/main/database-model.pdf)
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




------

[<img src="https://avatars.githubusercontent.com/carlosbetiol" width=115><br><sub>Carlos Betiol</sub>](https://github.com/carlosbetiol)

Linkedin: https://www.linkedin.com/in/carlosbetiol/

Email: carlos@betiol.dev
