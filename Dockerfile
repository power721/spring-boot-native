FROM alpine:edge

WORKDIR /app

COPY target/spring-boot-native ./app

ENTRYPOINT ["./app"]
