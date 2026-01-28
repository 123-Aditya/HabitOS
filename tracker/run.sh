#!/bin/bash
set -e

cd /Users/adityaupadhyaya/Projects/HabitOS/tracker

echo "Loading environment variables..."
set -a
source .env
set +a

echo "Cleaning & building project (skipping the tests)..."
mvn clean install -DskipTests

echo "Starting Spring Boot application..."
mvn spring-boot:run
