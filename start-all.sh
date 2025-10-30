#!/bin/bash

# ============================================
# Employee Management System - Quick Start Script
# ============================================

echo "=========================================="
echo "Employee Management System - Starting..."
echo "=========================================="

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to check if a service is running
check_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=0

    echo -e "${YELLOW}Waiting for $service_name to start...${NC}"

    while [ $attempt -lt $max_attempts ]; do
        if curl -s "$url" > /dev/null 2>&1; then
            echo -e "${GREEN}✓ $service_name is running${NC}"
            return 0
        fi
        attempt=$((attempt + 1))
        sleep 2
    done

    echo -e "${RED}✗ $service_name failed to start${NC}"
    return 1
}

# Step 1: Check prerequisites
echo ""
echo "Step 1: Checking prerequisites..."
echo "-----------------------------------"

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java is not installed. Please install Java 17+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java is installed${NC}"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}✗ Maven is not installed. Please install Maven 3.8+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven is installed${NC}"

# Check PostgreSQL
if ! command -v psql &> /dev/null; then
    echo -e "${YELLOW}⚠ PostgreSQL client is not installed${NC}"
else
    echo -e "${GREEN}✓ PostgreSQL is installed${NC}"
fi

# Step 2: Setup Configuration Repository
echo ""
echo "Step 2: Setting up configuration repository..."
echo "-----------------------------------"

CONFIG_REPO_PATH="$HOME/ems-config-repo"

if [ ! -d "$CONFIG_REPO_PATH" ]; then
    echo "Creating configuration repository at $CONFIG_REPO_PATH"
    mkdir -p "$CONFIG_REPO_PATH"
    cd "$CONFIG_REPO_PATH"
    git init

    # Copy config files (assumes they exist in project root/config)
    if [ -d "../config" ]; then
        cp ../config/*.yml .
        git add .
        git commit -m "Initial configuration"
        echo -e "${GREEN}✓ Configuration repository created${NC}"
    else
        echo -e "${YELLOW}⚠ Config files not found. Please create them manually${NC}"
    fi
    cd -
else
    echo -e "${GREEN}✓ Configuration repository already exists${NC}"
fi

# Step 3: Build all services
echo ""
echo "Step 3: Building all services..."
echo "-----------------------------------"

SERVICES=("discovery-service" "config-server" "gateway-service" "auth-service" "employee-service")

for service in "${SERVICES[@]}"; do
    if [ -d "$service" ]; then
        echo "Building $service..."
        cd "$service"
        mvn clean install -DskipTests
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ $service built successfully${NC}"
        else
            echo -e "${RED}✗ Failed to build $service${NC}"
            exit 1
        fi
        cd ..
    else
        echo -e "${RED}✗ $service directory not found${NC}"
        exit 1
    fi
done

# Step 4: Start services
echo ""
echo "Step 4: Starting services..."
echo "-----------------------------------"

# Start Discovery Service
echo "Starting Discovery Service..."
cd discovery-service
mvn spring-boot:run > ../logs/discovery-service.log 2>&1 &
DISCOVERY_PID=$!
cd ..
check_service "http://localhost:8761/actuator/health" "Discovery Service" || exit 1

# Start Config Server
echo "Starting Config Server..."
cd config-server
mvn spring-boot:run > ../logs/config-server.log 2>&1 &
CONFIG_PID=$!
cd ..
check_service "http://localhost:8888/actuator/health" "Config Server" || exit 1

# Start Gateway Service
echo "Starting Gateway Service..."
cd gateway-service
mvn spring-boot:run > ../logs/gateway-service.log 2>&1 &
GATEWAY_PID=$!
cd ..
check_service "http://localhost:8080/actuator/health" "Gateway Service" || exit 1

# Start Auth Service
echo "Starting Auth Service..."
cd auth-service
mvn spring-boot:run > ../logs/auth-service.log 2>&1 &
AUTH_PID=$!
cd ..
check_service "http://localhost:8081/actuator/health" "Auth Service" || exit 1

# Start Employee Service
echo "Starting Employee Service..."
cd employee-service
mvn spring-boot:run > ../logs/employee-service.log 2>&1 &
EMPLOYEE_PID=$!
cd ..
check_service "http://localhost:8082/actuator/health" "Employee Service" || exit 1

# Step 5: Display status
echo ""
echo "=========================================="
echo "All services started successfully!"
echo "=========================================="
echo ""
echo "Service URLs:"
echo "  Discovery Service: http://localhost:8761"
echo "  Config Server:     http://localhost:8888"
echo "  API Gateway:       http://localhost:8080"
echo "  Auth Service:      http://localhost:8081/swagger-ui.html"
echo "  Employee Service:  http://localhost:8082/swagger-ui.html"
echo ""
echo "Process IDs:"
echo "  Discovery: $DISCOVERY_PID"
echo "  Config:    $CONFIG_PID"
echo "  Gateway:   $GATEWAY_PID"
echo "  Auth:      $AUTH_PID"
echo "  Employee:  $EMPLOYEE_PID"
echo ""
echo "Logs directory: ./logs"
echo ""
echo "Default Admin Credentials:"
echo "  Email: admin@darumng.com"
echo "  Password: Admin@123"
echo ""
echo "To stop all services, run: ./stop-all.sh"
echo "=========================================="

# Save PIDs to file
echo "$DISCOVERY_PID" > .pids
echo "$CONFIG_PID" >> .pids
echo "$GATEWAY_PID" >> .pids
echo "$AUTH_PID" >> .pids
echo "$EMPLOYEE_PID" >> .pids

# Create stop script
cat > stop-all.sh << 'EOF'
#!/bin/bash

echo "Stopping all services..."

if [ -f .pids ]; then
    while read pid; do
        if ps -p $pid > /dev/null 2>&1; then
            echo "Stopping process $pid"
            kill $pid
        fi
    done < .pids
    rm .pids
    echo "All services stopped"
else
    echo "No PIDs file found"
fi
EOF

chmod +x stop-all.sh

echo "Press Ctrl+C to stop monitoring (services will continue running)"
echo "Or run ./stop-all.sh to stop all services"
echo ""

# Keep script running
tail -f /dev/null