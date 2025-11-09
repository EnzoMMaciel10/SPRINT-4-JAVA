FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw -DskipTests package || (apt-get update && apt-get install -y maven && mvn -DskipTests package)

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/apoia-1.0.0.jar app.jar
ENV PORT=8080
EXPOSE 8080
CMD ["sh","-c","java -jar app.jar"]
