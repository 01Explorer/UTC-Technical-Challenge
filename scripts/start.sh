#!/bin/bash

# Colors for better visibility
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting Tobacco Management System...${NC}"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Docker is not installed. Please install Docker and try again."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

# Check if all necessary files exist
if [ ! -f "Dockerfile" ]; then
    echo "Dockerfile not found. Please make sure it exists in the current directory."
    exit 1
fi

if [ ! -f "docker-compose.yml" ]; then
    echo "docker-compose.yml not found. Please make sure it exists in the current directory."
    exit 1
fi

if [ ! -f "CreateDB.sql" ]; then
    echo "CreateDB.sql not found. Please make sure it exists in the current directory."
    exit 1
fi

echo "Checking for running containers..."
# Stop any existing containers with the same names
docker-compose down

echo "Building and starting containers..."
# Build and start the containers in detached mode
docker-compose up --build -d

# Wait for the application to start
echo "Waiting for the application to start..."
sleep 10

# Check if the application started successfully
if docker-compose ps | grep -q "Up"; then
    echo -e "${GREEN}✓ Application started successfully!${NC}"
    echo -e "${GREEN}✓ Database initialized with TOBACCO schema${NC}"
    echo -e "\nApplication is running at: http://localhost:8080"
    echo "PostgreSQL database is available at: localhost:5432"
    echo "  - Username: postgres"
    echo "  - Password: 111111"
    echo "  - Schema: TOBACCO"
    echo -e "\nTo stop the application, run: ./stop.sh or docker-compose down"
else
    echo "There was an issue starting the application. Please check the logs."
    docker-compose logs
fi