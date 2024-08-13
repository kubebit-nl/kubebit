#
FROM eclipse-temurin:21.0.4_7-jre-alpine

#
EXPOSE 8801

#
RUN apk add --no-cache helm

#
WORKDIR /home

#
RUN mkdir /.kubebit

#
COPY /source/infrastructure/target/infrastructure-*-SNAPSHOT.jar /home/app.jar

#
CMD ["java", "-jar", "/home/app.jar"]