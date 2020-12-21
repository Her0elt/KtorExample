format:
	./gradlew ktlinFormat

fresh:
	make clean
	docker-compose build
	docker-compose up

clean:
	./gradlew clean build

run:
	./gradlew build
	docker-compose build
	docker-compose up

test:
	docker-compose run ./gradlew test
