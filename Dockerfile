# Get image maven to build the .jar file
FROM maven AS build
WORKDIR /app
COPY . /app
# Database variables
ARG DATABASE_DRIVER
ARG DATABASE_URL
ARG DATABASE_USERNAME
ARG DATABASE_PASSWORD
# ORM variables
ARG DDL_MODE
RUN mvn clean install

# get image containing OpenJDK 17 runtime for running the .jar file
# we use multi stage builds to reduce the image size and hide the variables
FROM openjdk:17
WORKDIR /app
EXPOSE 8080
# copy just the .jar file,
# so the environment variables in build are forgotten and
# the image will not contain any boilerplate files
COPY --from=build /app/target/BilliardsAPI-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]