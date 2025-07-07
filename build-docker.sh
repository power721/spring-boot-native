mvn clean package -DskipTests -s ~/.m2/empty-settings.xml

docker run --rm -v $(pwd):/work -w /work ghcr.io/graalvm/native-image-community:24-muslib \
  native-image --no-fallback --static --libc=musl --enable-http --enable-https -march=compatibility \
  -jar target/spring-boot-native-0.0.1.jar
