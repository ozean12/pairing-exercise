set -xe

docker-compose down
./gradlew clean build -x test
docker-compose build service
docker-compose up -d