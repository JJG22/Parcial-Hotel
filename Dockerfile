# Etapa 1 (build): compila el .jar con Maven Wrapper y JDK 21
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Primero solo los archivos de Maven, para cachear las dependencias
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline

# Luego el código fuente y el empaquetado
COPY src src
RUN ./mvnw -q -DskipTests package

# Etapa 2 (runtime): imagen liviana solo con el JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render define la variable PORT; la aplicación la lee en application.properties
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
