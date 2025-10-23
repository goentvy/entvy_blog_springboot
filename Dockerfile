FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . /app

RUN ./gradlew build -x test

CMD ["java", "-jar", "build/libs/blog-0.0.1-SNAPSHOT.jar"]