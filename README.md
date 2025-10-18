# 📦 Sistema de Inventário - Backend

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.8-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5-green.svg)](https://junit.org/junit5/)

## 🎯 Visão Geral

Sistema de gerenciamento de inventário desenvolvido em Spring Boot com funcionalidades completas para controle de categorias e produtos. O sistema oferece APIs RESTful, exportação para Excel, upload de imagens com compressão e testes automatizados.

## ✨ Funcionalidades

### 🏷️ Gestão de Categorias
- ✅ CRUD completo (Create, Read, Update, Delete)
- ✅ Busca por ID e listagem geral
- ✅ Exportação para Excel
- ✅ Validações de dados

### 📦 Gestão de Produtos
- ✅ CRUD completo com upload de imagens
- ✅ Compressão automática de imagens (ZLib)
- ✅ Validação de tamanho (máx. 10MB) e tipo de arquivo
- ✅ Busca por nome (case-insensitive)
- ✅ Busca por ID e listagem geral
- ✅ Exportação para Excel
- ✅ Associação com categorias

### 🔧 Funcionalidades Técnicas
- ✅ APIs RESTful com documentação
- ✅ Compressão de imagens para otimização de espaço
- ✅ Validação robusta de dados
- ✅ Tratamento de erros padronizado
- ✅ Testes unitários e de integração
- ✅ CORS configurado para frontend

## 🏗️ Arquitetura

```
src/
├── main/java/com/company/inventory/inventario/
│   ├── controller/          # REST Controllers
│   │   ├── CategoryRestController.java
│   │   └── ProductRestController.java
│   ├── dao/                 # Data Access Objects
│   │   ├── ICategoryDao.java
│   │   └── IProductDao.java
│   ├── model/               # Entidades JPA
│   │   ├── Category.java
│   │   └── Product.java
│   ├── response/            # DTOs de Resposta
│   │   ├── CategoryResponse.java
│   │   ├── CategoryResponseRest.java
│   │   ├── ProductResponse.java
│   │   ├── ProductResponseRest.java
│   │   └── ResponseRest.java
│   ├── services/            # Camada de Serviços
│   │   ├── ICategoryService.java
│   │   ├── IProductService.java
│   │   └── impl/
│   │       ├── CategoryServiceImpl.java
│   │       └── ProductServiceImpl.java
│   └── util/                # Utilitários
│       ├── CategoryExcelExporter.java
│       ├── ProductExcelExporter.java
│       └── Util.java
└── test/java/               # Testes
    └── com/company/inventory/inventario/
        └── controller/
            ├── CategoryRestControllerTest.java
            └── ProductRestControllerTest.java
```

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.4.8** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Web** - APIs REST
- **MySQL 8.0** - Banco de dados
- **Maven** - Gerenciamento de dependências

### Testes
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking de dependências
- **Spring Test** - Testes de integração

### Utilitários
- **Apache POI** - Exportação para Excel
- **Lombok** - Redução de boilerplate
- **ZLib** - Compressão de imagens

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- MySQL 8.0+
- Git

## 🛠️ Instalação e Configuração

### 1. Clone o repositório
```bash
git clone https://github.com/Adson-C/Inventario-backend.git
cd Inventario-backend
```

### 2. Configure o banco de dados
Crie um banco de dados MySQL:
```sql
CREATE DATABASE db_inventario;
```

### 3. Configure as credenciais
Edite o arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_inventario?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 4. Execute o projeto
```bash
# Compilar o projeto
mvn clean compile

# Executar os testes
mvn test

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8081`

## 📚 API Endpoints

### 🏷️ Categorias

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/categories` | Listar todas as categorias |
| `GET` | `/api/v1/categories/{id}` | Buscar categoria por ID |
| `POST` | `/api/v1/categories` | Criar nova categoria |
| `PUT` | `/api/v1/categories/{id}` | Atualizar categoria |
| `DELETE` | `/api/v1/categories/{id}` | Deletar categoria |
| `GET` | `/api/v1/categories/export/excel` | Exportar categorias para Excel |

### 📦 Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/products` | Listar todos os produtos |
| `GET` | `/api/v1/products/{id}` | Buscar produto por ID |
| `GET` | `/api/v1/products/filter/{name}` | Buscar produtos por nome |
| `POST` | `/api/v1/products` | Criar novo produto |
| `PUT` | `/api/v1/products/{id}` | Atualizar produto |
| `DELETE` | `/api/v1/products/{id}` | Deletar produto |
| `GET` | `/api/v1/products/export/excel` | Exportar produtos para Excel |

## 📝 Exemplos de Uso

### Criar uma categoria
```bash
curl -X POST http://localhost:8081/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Eletrônicos",
    "description": "Produtos eletrônicos em geral"
  }'
```

### Criar um produto
```bash
curl -X POST http://localhost:8081/api/v1/products \
  -F "picture=@imagem.jpg" \
  -F "name=Smartphone" \
  -F "price=1500" \
  -F "account=10" \
  -F "categoryId=1"
```

### Buscar produtos por nome
```bash
curl -X GET "http://localhost:8081/api/v1/products/filter/smartphone"
```

## 🧪 Testes

O projeto inclui testes abrangentes para todos os controllers:

### Executar todos os testes
```bash
mvn test
```

### Executar testes específicos
```bash
# Testes de categoria
mvn test -Dtest=CategoryRestControllerTest

# Testes de produto
mvn test -Dtest=ProductRestControllerTest
```

### Cobertura de testes
- ✅ **CategoryRestController**: 15+ métodos de teste
- ✅ **ProductRestController**: 20+ métodos de teste
- ✅ Cenários: Sucesso, erro, validação, não encontrado
- ✅ Tipos: Unitários, integração, validação

## 📊 Estrutura do Banco de Dados

### Tabela: categories
```sql
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);
```

### Tabela: products
```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    account INT NOT NULL,
    picture LONGBLOB,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

## 🔧 Configurações Avançadas

### Compressão de Imagens
O sistema comprime automaticamente as imagens usando ZLib para otimizar o armazenamento:
- Tamanho máximo: 10MB
- Tipos aceitos: image/*
- Compressão automática antes do salvamento

### Exportação Excel
- Formato: .xlsx
- Categorias: `result_category.xlsx`
- Produtos: `result_product.xlsx`
- Headers automáticos com formatação

## 🚀 Deploy

### Docker (Recomendado)
```bash
# Build da imagem
docker build -t inventario-backend .

# Executar container
docker run -p 8081:8081 inventario-backend
```

### JAR Executável
```bash
# Gerar JAR
mvn clean package

# Executar
java -jar target/inventario-0.0.1-SNAPSHOT.jar
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Autor

**Adson Sa**
- GitHub: [@Adson-C](https://github.com/Adson-C)
- LinkedIn: [Adson Sa](https://linkedin.com/in/adson-sa)

## 🙏 Agradecimentos

- Spring Boot Team
- Apache POI
- JUnit Team
- MySQL Community

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela!** ⭐