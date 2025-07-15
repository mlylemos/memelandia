
# 🧠 Memelândia - Microsserviços com Spring Boot

Memelândia é uma aplicação de exemplo que demonstra como desmembrar um sistema monolítico em uma arquitetura baseada em **microsserviços** usando **Spring Boot**, **Docker**, **Maven** e ferramentas de monitoramento como **Prometheus**.

## 📦 Estrutura do Projeto

```
memelandia-parent/
├── docker-compose.yml           # Orquestração dos serviços
├── init-db.sql                  # Script SQL para inicialização do banco
├── prometheus.yml               # Configuração do Prometheus para monitoramento
├── test.sh                      # Script para rodar testes automatizados
│
├── category-service/            # Microsserviço para categorias de memes
├── meme-service/                # Microsserviço principal com CRUD e "Meme do Dia"
├── user-service/                # Microsserviço de autenticação/usuários
├── shared-config/               # Configurações Spring Cloud centralizadas
```

## 🚀 Como Executar

1. **Pré-requisitos**:
   - Docker e Docker Compose instalados
   - Java 17+ instalado
   - Maven 3.8+

2. **Suba os serviços com Docker Compose. Abra o aplicativo Docker Compose e execute o seguinte código no terminal da pasta do projeto**:

```bash
docker-compose up --build
```

3. Acesse os serviços em:

- User Service: `http://localhost:8081/api/users`
- Category Service: `http://localhost:8082/api/categories`
- Meme Service: `http://localhost:8083/memes`
- Prometheus: `http://localhost:9090`

## 🧪 Executando Testes

O projeto contém testes automatizados para cada microsserviço.

Para executar os testes:

```bash
./test.sh
```

Ou manualmente em cada serviço:

```bash
cd user-service
mvn test

cd ../category-service
mvn test

cd meme-service
mvn test
```

## 🛠 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Web
- Docker & Docker Compose
- Prometheus
- Maven (multi-módulo)
- H2 / MySQL

## 🧠 Funcionalidades

- Registro e consulta de usuários (user-service)
- Cadastro e busca de categorias (category-service)
- Cadastro, busca e sorteio de "Meme do Dia" (meme-service)
- Monitoramento com Prometheus
- Comunicação entre serviços via REST

## 📂 Configuração Centralizada

O módulo `shared-config` contém propriedades comuns a todos os serviços, simplificando a configuração e facilitando a manutenção.

## ✅ Projeto final do curso EBAC Backend Java

Este projeto faz parte do desafio final do curso da **EBAC** para demonstrar a aplicação de boas práticas com microsserviços.

---

**Desenvolvido por Emily Lemos ✨**
