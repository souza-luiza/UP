# Estágio 1: Build da aplicação usando Maven com Java 21
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Copia arquivos de configuração de dependências
COPY pom.xml .

# Baixa as dependências em cache para acelerar builds futuros
RUN mvn dependency:go-offline -B

# Copia o código-fonte e gera o pacote compilado desconsiderando os testes
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final leve para execução (JRE 21)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build
COPY --from=build /app/target/up-back-0.0.1-SNAPSHOT.jar app.jar

# Informa a porta padrão que o Render monitora
EXPOSE 8080

# Define a variável de ambiente para garantir que o Spring escute na porta correta
ENV PORT=8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]