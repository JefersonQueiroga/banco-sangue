# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiar arquivos do Maven Wrapper
COPY mvnw .
COPY .mvn .mvn

# Copiar pom.xml
COPY pom.xml .

# Tornar mvnw executável
RUN chmod +x mvnw

# Baixar dependências (cache layer)
RUN ./mvnw dependency:go-offline --batch-mode || true

# Copiar código fonte
COPY src src

# Build da aplicação
RUN ./mvnw clean package -DskipTests --batch-mode

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Criar usuário não-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

# Expor porta
EXPOSE 8080

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]