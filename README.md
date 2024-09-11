# API Golden Raspberry Awards






## Desenvolvedor

[<img src="https://avatars.githubusercontent.com/carlosbetiol" width=115><br><sub>Carlos Betiol</sub>](https://github.com/carlosbetiol)

Linkedin: https://www.linkedin.com/in/carlosbetiol/

Email: carlos@betiol.dev



Objetivo: Desenvolvimento de API de Premiações de Filmes

Documento de especificação: [Especificacao Backend.pdf](https://github.com/carlosbetiol/GRA_Outsera/tree/main/Especificação Backend.pdf)

Github Project: [Project Roadmap](https://github.com/users/carlosbetiol/projects/1)




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

* [Instruções de uso com OAUTH2 ativado](#Instruções-de-uso-com-OAUTH2-ativado)

* [Respostas](#respostas)

* [Exemplo de resposta](#Exemplo-de-resposta)

* [Referências utilizadas](#Referências-utilizadas)

  

## Escopo

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



## Dados técnicos

Este projeto foi desenvolvido utilizando a IDE IntelliJ e codificado segundo técnicas da modelagem DDD de construções de aplicações.

Foi utilizado o JAVA 17, Spring Boot 3.3.3 e Oauth2 Authorization Server 1.3.2 utilizando o fluxo Authorization Code com PKCE para autenticação.

Maven foi o gerenciador de pacotes utilizado para o projeto na versao 3.9.6.

Todos os parâmetros como: dados de conexão com o banco de dados, porta a ser utilizada, whitelist de endereços de IP, dentre outros, deverão ser enviados como variáveis de ambiente.



## Banco de dados

Utilizado o H2 em memória e populado de acordo com arquivo CSV que deve ser incluido com o nome **movielist.csv** na pasta **src/main/resources/data**.

Estrutura do banco de dados: [database-model.pdf](https://github.com/carlosbetiol/GRA_Outsera/tree/main/database-model.pdf)



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

**senha: 2Enc$738**



## Utilização via Postman

Para testes via Postman, pode ser feito o import do json abaixo:

- [GRA.postman collection.json](https://github.com/carlosbetiol/GRA_Outsera/tree/main/GRA.postman_collection.json)



## Instruções de uso com OAUTH2 ativado

Quando a API estiver rodando no perfil "prod", a autenticação do usuário será necessária para que seja possível os o acesso aos endpoints quando utilizados via POSTMAN ou SWAGGER.

O fluxo utilizado na API é o **Authorization Code with PKCE**.

**Importante**: Devido a características de segurança quando se utiliza o Spring Oauth2 Authorization Server, não será possível a autenticação quando utilizado o http://localhost:8080 ou http://127.0.0.1:8080. Portanto, será necessário colocar uma entrada local na configuração de **hosts** com algum nome, por exemplo, **host.local** que deverá ser apontado para 127.0.0.1. Sendo assim, todos os exemplos e instruções desta seção estarão apontando para http://host.local:8080.

#### Glossário

- **code**: é o authorization code, cõdigo de authorização que é ercebido por uma URL de retorno e utilizado para requisitar o token JWT.

- **code_verifier**: o cliente deve gerar uma cadeia de caracteres com tamanho recomendado de 40 caracteres contendo letras maiúsculas, minúsculas e nḿeros de forma aleatória. Essa cadeia será usada na chamada de requisição do token. Por segurança, deve ser gerado um novo a cada tentativa de autenticação, **mas para facilitar testes**, torna-se melhor sempre usar o mesmo.

- **PKCE**: é um code challenge (desafio de código) que é gerado a partir do **code_verifier**. Para isso pode ser utilizada alguma biblioteca geraora de **PKCE**. Para gerar manualmente por motivo de testes, pode ser usado o site [tonyxu-io pkce generator](https://tonyxu-io.github.io/pkce-generator/). O **PKCE** é usado somente na chamada **GET** com objetivo de receber o **authorization_code**.

- **response_type**: corresponde ao tipo do fluxo utilizado para a autorização, no caso serã sempre **code**.

- **client_id**: é o id de autenticação do cliente na API. Como **default**, que pode ser configado via ENV, estã configurado conforme credenciais abaixo:

  **client_id: frontweb**

  **password: 123456**

- **state**: é uma cadeia de caracteres qualquer que deverá ser enviada para requisitar o **authorization_code** e deve ser armazenada no cliente para verificação de sua validade na URL de retorno. Deve ser gerado um novo state a cada tentativa de autenticação. Entretanto, para efeitos de **facilitar testes manuais**, a mesma cadeia pode ser usada em todas as chamadas.

  É usual enviar no state uma cadeia de caracteres que inclua a **URL** requisitada pelo cliente, para efeitos de redirect depois de autenticado. Nesse caso, a conversão para **Base64** e um **parse** se mostram eficientes.

- **redirect_uri**: é uma **URI** de retorno que deve ser implementada pelo **frontend**, onde será recebido o **code** e o **state**. O **state** **recebido** deve ser comparado ao **state** **enviado** e a autenticação deve ser rejeitada caso diferentes. A **redirect_uri** deve estar liberada na API, portanto, para acesso local teremos a seguinte: **http://localhost:3000/oauth2-redirect.html**, que não precisa existir para efeitos de realizar testes manuais.

- **scope**: é o escopo permitido para o usuário quando autenticado, deve ser usado sempre a cadeia de caracteres **"READ WRITE"** que a API, através destes escopos determina as **permissões** de escrita e leitura para o usuário.

- **code_challenge**: é o **PKCE** já descrito anteriormente.

- **code_challenge_method**: é o método de criptografia do code_challenge. Para a API, deve ser usado **S256**.



#### Descrição e exemplos do fluxo completo para obtenção de token JWT

- Primeiramente, é necessário obter um **authorization_code** (code) via requisição **GET** via c**hamada de navegador**. Para isso vamos utilizar os valores abaixo:

  - *code_verifier*: **qP4LZb5R8XSJS5-bPigiKXYbocRzXe3EaXO95GFPVQQ**
  - *code_chalenge*: **VNnuCNePadKYfDOytzfMTBO2RodOY5UbepKaIkUNIms**  (é calculado através do code_verifier utilizando o site [tonyxu-io pkce generator](https://tonyxu-io.github.io/pkce-generator/))
  - *redirect_uri*: http://localhost:3000/oauth2-redirect.html (não existe, mas é suficiente para recebermos o code que é mostrado na barra de endereço do navegador)
  - state: **6bdUgypabccwXhIk8BhJEUScm-pqEGUHLrxPs7sC4yA**

  Com os dados acima, deve ser feita uma requisição **GET no novegador** com a seguinte URL:

  ```
  http://host.local:8080/oauth2/authorize?response_type=code&client_id=frontweb&state=6bdUgypabccwXhIk8BhJEUScm-pqEGUHLrxPs7sC4yA&redirect_uri=http://localhost:3000/oauth2-redirect.html&scope=READ WRITE&code_challenge=VNnuCNePadKYfDOytzfMTBO2RodOY5UbepKaIkUNIms&code_challenge_method=S256
  ```

  Ao chamar essa url no navegador, a API vai abrir tela de login para autenticação, onde deve ser usadas as credenciais abaixo:

  **login: admin@gra.com**

  **senha: 2Enc$738**

  ![Tela de login](https://github.com/carlosbetiol/GRA_Outsera/blob/main/GoldenRaspberryAwards/src/main/resources/static/assets/images/login-screen.png)

  

  A tela seguinte é tentativa de **redirect** realizada pelo **authorization** **server**, onde na **barra de endereço** vem na URL o authorization_code (code). Esse code deve ser copiado e reservado para que seja possível solicitar um token. No caso de desenvolvimento de um **frontend**, essa chamada serve para passar o code para prosseguir com a autenticação.

  ![URL com o code](https://github.com/carlosbetiol/GRA_Outsera/blob/main/GoldenRaspberryAwards/src/main/resources/static/assets/images/code-screen.png)

  O frontend deve comparar o state recebido na url com o state enviado. 

  Code recebido:
  
  ```
  3l26sjlgjV7V3F8OH2XZLN6SN-FqOhKvS3ZT5f5edgbS4CkQzFqqn9nqg2UBPyPx4S4y8AsSabvYcE-wnnwye-5iQVV4RA_WGYXqQvyWsC9XiKMmxT1DGp-kDAU2dPbT
  ```



- De posse do **code**, através de uma chamada **POST** para a API (no endpoint http://host.local:8080/oauth2/token), um **token JWT** e um **refresh_token** podem ser requisitados. Para isso, além do code vamos utilizar os valores abaixo no body da requisição que poderão ser do tipo **form-data** ou **x-www-form-urlencoded**:
  - grant_type: **authorization_code**
  - code: **o code reservado anteriormente**
  - redirect_uri: http://localhost:3000/oauth2-redirect.html (a mesma utilizada para pegar o code)
  - code_verifier: **qP4LZb5R8XSJS5-bPigiKXYbocRzXe3EaXO95GFPVQQ** (o mesmo utilizado para pegar o code)

​	![Corpo da request POST](https://github.com/carlosbetiol/GRA_Outsera/blob/main/GoldenRaspberryAwards/src/main/resources/static/assets/images/post1-screen.png)

Em adição ao corpo, na mesma requisição **POST** para solicitar um **token JWT**, é necessário enviar uma entrada header **Authorization**, do tipo **Basic**, enviando o client_id e password como credenciais da aplicação. Entretanto o Authorization é composto do client_id e password no formato **client_id:password** e deve ser convertido em base64.

Considerando que as credenciais definidas como padrão para a aplicação são:

**client_id: frontweb**

**password: 123456**

Deve ser gerado o base64 de " **frontweb:123456**" que corresponde a **ZnJvbnR3ZWI6MTIzNDU2** (Ferramentas como [base64encode](https://www.base64encode.org/), podem ajudar).

Sendo assim, a entrada no header da requisição POST deve ser:
**Authorization: Basic ZnJvbnR3ZWI6MTIzNDU2**

![Header da request POST](https://github.com/carlosbetiol/GRA_Outsera/blob/main/GoldenRaspberryAwards/src/main/resources/static/assets/images/post2-screen.png)

> [!TIP]
>
> O **Postman** já converte para base 64 ao adicionar o nome de usuário e senha na aba **Authorization** com o tipo **Basic Auth**
>
> ![Authorization request POST](https://github.com/carlosbetiol/GRA_Outsera/blob/main/GoldenRaspberryAwards/src/main/resources/static/assets/images/post3-screen.png)



Ao realizar o POST, o JWT é recebido.

**Importante**: Uma vez feita a request com um authorization_code, **se ela falhar,** ele é invalidado, não poderá mais ser usado e outro deve ser solicitado através do fluxo que utiliza o GET descrito anteriormente. O mesmo acontece **se houver muita demora** em buscar o requisitar o tokem com aquele code.

![JWT token](https://github.com/carlosbetiol/GRA_Outsera/blob/main/GoldenRaspberryAwards/src/main/resources/static/assets/images/post4-screen.png)

O JWT contém todas as permissões de usuários e com ele será possível realizar as requisições na API utilizando-o no header como Bearer conforme tela abaixo:

![JWT use](https://github.com/carlosbetiol/GRA_Outsera/blob/main/GoldenRaspberryAwards/src/main/resources/static/assets/images/api1-screen.png)



> [!NOTE]
>
> Falta descrever o fluxo para pegar um novo token via refresh_token.




------

[<img src="https://avatars.githubusercontent.com/carlosbetiol" width=115><br><sub>Carlos Betiol</sub>](https://github.com/carlosbetiol)

Linkedin: https://www.linkedin.com/in/carlosbetiol/

Email: carlos@betiol.dev
