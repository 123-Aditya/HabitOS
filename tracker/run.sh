#!/bin/bash
set -e

echo "Loading environment variables..."
set -a
source .env
set +a

echo "Cleaning & building project..."
mvn clean install

echo "Starting Spring Boot application..."
mvn spring-boot:run
