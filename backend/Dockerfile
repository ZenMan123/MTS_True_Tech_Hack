FROM openjdk:latest

WORKDIR /app

COPY target/wallet-1.0.jar /app/wallet-1.0.jar

EXPOSE 8090
EXPOSE 8091

CMD ["java", "-jar", "wallet-1.0.jar"]