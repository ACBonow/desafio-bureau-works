# Desafio Técnico - FullStack
## Objetivo
O principal objetivo do sistema é fornecer uma plataforma abrangente para gerenciar informações de tradutores e documentos, facilitando a interação eficiente e o controle desses dados de forma escalável.

## Considerações
Foi desenvolvido um sistema de gerenciamento de tradutores e documentos, com as seguintes funcionalidades:
internacionalização do Frontend - opção de Português ou Inglês
CRUD de tradutores
CRUD de documentos
inserção de documentos por arquivo CSV.
inserção de tradutores por arquivo CSV.

O sistema foi desenvolvido utilizando Java 21 com Spring Boot no backend e Vue.js no frontend.

O sistema foi desenvolvido com o objetivo de ser simples e fácil de usar, com uma interface amigável e intuitiva.

Embora não seja o ideal, optei por não utilizar autenticação e autorização, pois o foco do desafio era desenvolver um sistema funcional e não complexo.

- É permitido o cadastro de Tradutores com o mesmo e-mail, devido estar no examplo de .CSV, embora seja recomendado que o e-mail seja único.
- É permitido o cadastro de Documentos, via .CSV ou individual, sem locate. quando isso ocorrer, o sistema irá gerar 
  um locate a partir da IA da OpenAI.
- Como não havia regra para settar o Tradutor responsável pelo documento, foi usado uma metodologia aleatória, onde o sistema irá 
  escolher um tradutor aleatoriamente para cada documento, SE o tradutor tiver o idioma de origem igual o idioma do 
  documento.
- Nos arquivos .CSV não havia uma padronização da abreviatura do idioma. 
  Exemplo: "pt-br","pt_br" e "pt_BR" são considerados o mesmo idioma. O sistema irá converter para o padrão ISO 
  "pt-BR"
- Se não houver um tradutor disponível para o idioma do documento, o sistema irá gerar um Exception e informar o 
  usuário.
- Um tradutor só pode ser excluido se não houverem documentos associados a esse tradutor

## Como rodar o projeto
clone o repositório: https://github.com/ACBonow/desafio-bureau-works.git

Configure o banco de dados

- Crie um banco de dados no PostgreSQL chamado "translator_manager"
- Crie um usuário com o nome "postgres" e senha "postgres"
- Se necessário, altere as credenciais no arquivo application.properties do backend

application.properties
   
    spring.datasource.url=jdbc:postgresql://localhost:5432/translator_manager
    spring.datasource.username=postgres
    spring.datasource.password=postgres

Rodando o BACKEND

    cd desafio-bureau-works/translator-manager-backend
    mvn clean install
    mvn spring-boot:run

- Backend disponível em: http://localhost:8080
- Acesse a documentação da API em http://localhost:8080/swagger-ui/index.html

Rodando o FRONTEND

    cd desafio-bureau-works/translator-manager-frontend
    npm install
    npm run serve

- Frontend disponível em: http://localhost:8081
- Se a porta 8081 já estiver em uso, o Vue CLI irá sugerir uma porta alternativa. Você pode acessar a aplicação na 
  nova porta sugerida, porém precisa-rá alterar a URL liberada do CORS no backend.

    netstat -ano | findstr :8081
    taskkill /PID <PID> /F
  
onde <PID> deve ser substituido pelo numero do processo que apareceu quando executou o primeiro comando

Path para documentos de teste
- desafio-bureau-works/translator-manager-backend/src/main/resources/documents.csv
- desafio-bureau-works/translator-manager-backend/src/main/resources/translators.csv


## Tecnologias Utilizadas
- Java 21 - https://www.oracle.com/br/java/technologies/downloads#java21
- Spring Boot 3.4.4 
- Maven 3.9.9 - https://maven.apache.org/download.cgi
- PostgreSQL 17 - https://www.postgresql.org/download/
- Node.js 22.14.0 - https://nodejs.org/en/download/
- Vue.js 3 

Dependêcias Backend
- spring-boot-starter: Configuração básica do Spring Boot, fornece suporte para inicialização rápida de aplicações Spring.
- spring-boot-starter-web: Inclui dependências para criar APIs REST e aplicações web.
- spring-boot-starter-data-jpa: Suporte para persistência de dados usando JPA (Java Persistence API) e integração com   bancos de dados.
- spring-boot-starter-validation: Fornece suporte para validação de dados usando anotações como @Valid e @NotNull.
- spring-boot-starter-test: Dependência para testes, incluindo JUnit, Mockito e ferramentas de teste do Spring.
- postgresql: Driver JDBC para conectar a aplicação ao banco de dados PostgreSQL.
- lombok: Biblioteca que reduz o código boilerplate em classes Java, gerando automaticamente getters, setters, construtores, etc.
- springdoc-openapi-starter-webmvc-ui: Gera automaticamente a documentação da API no formato OpenAPI/Swagger, permitindo visualização e testes interativos.

Dependêcias Frontend
- @vueuse/core: Uma coleção de utilitários baseados em Vue 3 que facilitam o desenvolvimento, como hooks reativos e funções úteis para manipulação de estado e interações.
- axios: Uma biblioteca para fazer requisições HTTP. É amplamente usada para comunicação com APIs, como enviar e receber dados do servidor.
- bootstrap: Um framework CSS popular para criar interfaces responsivas e estilizadas. Ele fornece componentes prontos, como botões, formulários e layouts.
- bootstrap-icons: Um conjunto de ícones compatível com o Bootstrap, que pode ser usado para adicionar ícones visuais ao projeto.
- core-js: Uma biblioteca que fornece polyfills para compatibilidade com navegadores mais antigos, garantindo que recursos modernos de JavaScript funcionem em ambientes legados.
- vue: O framework principal utilizado no projeto. É uma biblioteca JavaScript para construir interfaces de usuário reativas e componentes reutilizáveis.
- vue-i18n: Uma biblioteca para internacionalização (i18n) em projetos Vue. Permite traduzir textos e adaptar o conteúdo para diferentes idiomas.
- vue-router: O roteador oficial do Vue, usado para gerenciar navegação entre páginas ou componentes em uma aplicação Vue.

Banco de Dados
- PostgreSQL: Um sistema de gerenciamento de banco de dados relacional e objeto-relacional.


     
