# Stage 1: Build the Quarkus application
#FROM maven:3.8.4-openjdk-11-slim AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests

# Stage 2: Create the final image
#FROM openjdk:11-jre-slim
#WORKDIR /app
#COPY --from=build /app/target/*.jar /app/app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Use the official OpenJDK 22 base image
# FROM openjdk:22-jdk

# Set the working directory
# WORKDIR /app

# Copy the JAR file to the container
# COPY target/quarkus-app/quarkus-run.jar /app/application.jar

# Expose the port (replace 8080 with your application's port)
# EXPOSE 8080

# Run the application
# CMD ["java", "-jar", "application.jar"]

FROM registry.access.redhat.com/ubi8/openjdk-21:1.19

ENV LANGUAGE='en_US:en'

# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]