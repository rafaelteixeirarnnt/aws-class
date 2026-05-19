FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/class3-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-jar", "app.jar"]