# Fronza Barber API

Aplicação para consulta e agendamento de barbeiros , usando Spring Boot + TravisCI.

[![Build Status](https://travis-ci.com/gustavo-flor/fronza-barber-api.svg?branch=main)](https://travis-ci.com/gustavo-flor/fronza-barber-api)
---

## Instalação

### Ambiente

- Instale o Java 12 (Suporte ao JDK 12.0.2). Segue o site da [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) ☕, onde estão disponíveis as versões adequadas para cada sistema operacional;

- Instale o Postgres 13. Sugiro a instalação via [Docker](https://hub.docker.com/_/postgres) 🐳 

> É importante notar que a aplicação esta setada para acessar o banco de dados via `localhost:5432` com usuário e senha `postgres`.

- Após a instalação do Postgres, acesse o seu banco de dados (via UI ou linha de comando), e execute:

```sql
CREATE DATABASE fronza-barber-api
```

#### Configuração do envio de email

> Para que o envio de e-mail funcione corretamente é necessário configurar os itens `spring.mail.username` e `spring.mail.password` presentes em `src > main > resources > application.yml`, esses valores são respectivamente o endereço de e-mail do google e a sua senha.

- Ativar opção de `Permitir aplicativos menos seguros` em [Configurações do Google](https://myaccount.google.com/u/1/lesssecureapps);

- Permitir acesso à sua Conta em [Configurações do Google](https://accounts.google.com/b/1/DisplayUnlockCaptcha);

> Uma outra opção é ativar o profile de `test` via `application.yml`, dessa forma os e-mails enviados pela aplicação serão *logados* no console.

### Deploy

Executar `./mvnw spring-boot:run` para executar o serviço.

> Também é possível executar o `./mvnw clean install` e executar o serviço através do `.jar` gerado na pasta `target`.

Para visualizar os endpoints disponiveis acessar o [Swagger](http://localhost:8080/api/swagger-ui/).
