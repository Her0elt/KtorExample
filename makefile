format:
	./gradlew ktlinFormat

fresh:
	make format
	./gradlew build

clean:
	./gradlew clean build

run:
	./gradlew run

auto:
	./gradlew -t installDist

test:
	./gradlew test

up:
	docker-compose up
