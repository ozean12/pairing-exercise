FROM adoptopenjdk/openjdk15 as base

RUN mkdir -p /opt/organisations
WORKDIR /opt/organisations

RUN apt-get update && apt-get install -y unzip

COPY ./build/distributions/organisations-0.0.1.zip /opt/organisations/organisations.zip
RUN unzip organisations.zip && rm organisations.zip

EXPOSE 8080

CMD ["java", "-classpath", "/opt/organisations/organisations-0.0.1/lib/*", "-Dspring.profiles.active=production", "io.billie.ApplicationKt"]
