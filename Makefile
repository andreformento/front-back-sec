#!make
include .env
export $(shell sed 's/=.*//' .env)

start-database:
	@docker-compose up -d

stop-database:
	@docker-compose stop -t 0
	@docker-compose rm -fv

recreate-database: stop-database start-database

test-api:
	@./mvnw clean test

run-api-8080: start-database
	@export APP_OAUTH2_REDIRECT_URI='http://localhost:8080/oauth2/redirect' && \
	 ./mvnw clean spring-boot:run

run-api-3000: start-database
	@./mvnw clean spring-boot:run

run-ui:
	@cd src/main/resources/frontend && \
	 npm install && \
	 export REACT_APP_OAUTH2_REDIRECT_URI='http://localhost:3000/oauth2/redirect' && \
	 npm start
