# Dockerfile
FROM eclipse-temurin:21-jre-alpine

# Criar usuário não-root por segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Diretório de trabalho
WORKDIR /app

# Copiar o JAR da aplicação
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expor porta
EXPOSE 8080

# Variáveis de ambiente (serão sobrescritas em produção)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]