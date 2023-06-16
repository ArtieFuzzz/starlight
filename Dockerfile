FROM gradle:7-jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:17.0.2

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/server.jar
EXPOSE 8080:8080
# Default port
ENV STARLIGHT_PORT=8080
ENTRYPOINT ["java","-jar","/app/server.jar"]