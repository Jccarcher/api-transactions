# Usar una imagen base de Java (Temurin JRE 21 disponible en Docker Hub)
FROM eclipse-temurin:21-jre

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR generado por tu build; el artefacto actual se llama rest-0.0.1.jar
COPY target/rest-0.0.1.jar app.jar

# Exponer el puerto que usa tu Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]