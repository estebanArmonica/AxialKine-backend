FROM eclipse-temurin:21.0.6_7-jdk
ARG JAR_FILE=target/AxialkineWeb-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_kineweb.jar
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "app_kineweb.jar"]