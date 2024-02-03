FROM eclipse-temurin:17-jre-alpine
COPY /target/offers.jar /offers.jar
ENTRYPOINT ["java","-jar","/offers.jar"]