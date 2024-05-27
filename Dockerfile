#
# Build and Package
#
FROM eclipse-temurin:22-jdk-jammy AS build

ENV HOME=/usr/app

RUN mkdir -p $HOME

WORKDIR $HOME

COPY . $HOME

RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package

#
# Run
#
FROM eclipse-temurin:22-jdk-jammy

ARG JAR_FILE=/usr/app/target/*.jar

ENV MYSQL_USER=root

COPY --from=build $JAR_FILE app.jar

ENTRYPOINT ["java","-jar","/app.jar"]