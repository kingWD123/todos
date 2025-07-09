FROM eclipse-temurin:17-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY target/todos.jar todos.jar
ENTRYPOINT ["java","-jar","/todos.jar"]