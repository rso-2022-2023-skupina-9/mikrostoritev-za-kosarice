FROM maven:3.8.6-openjdk-18 AS build
COPY ./ /app
WORKDIR /app
RUN mvn --show-version --update-snapshots --batch-mode clean package

FROM amazoncorretto:18
RUN mkdir /app
WORKDIR /app
COPY --from=build ./app/api/target/api-1.0.0-SNAPSHOT.jar /app
EXPOSE 8081
CMD ["java", "-jar", "api-1.0.0-SNAPSHOT.jar"]