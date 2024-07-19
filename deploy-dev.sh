#!/bin/bash

# Remove local target/ directory
rm -rf target/
echo "Deleted target/ folder"

# Build the Spring Boot application with Maven
./mvnw -Pdev package -DskipTests
echo "Generated jar file"

# SSH Key location (adjust path as per your environment)
SSH_KEY="C:\Users\hndhl\Downloads\brighthr-backend_key.pem"

# Azure VM instance details
AZURE_INSTANCE="azureuser@52.146.5.214"

# Remote directory on Azure VM instance
REMOTE_DIR="/home/azureuser"

# Remove existing jar file on server if it exists
ssh -i "$SSH_KEY" "$AZURE_INSTANCE" "[ -f $REMOTE_DIR/brighthr-0.0.1-SNAPSHOT.jar ] && rm $REMOTE_DIR/brighthr-0.0.1-SNAPSHOT.jar"
echo "Deleted existing jar file on Azure VM if it existed"

# Remove existing execute_commands_on_azure.sh file on server if it exists
ssh -i "$SSH_KEY" "$AZURE_INSTANCE" "[ -f $REMOTE_DIR/execute_commands_on_azure.sh ] && rm $REMOTE_DIR/execute_commands_on_azure.sh"
echo "Deleted existing execute_commands_on_azure.sh file on Azure VM if it existed"

# Copy execute_commands_on_azure.sh file to server
scp -i "$SSH_KEY" execute_commands_on_azure.sh "$AZURE_INSTANCE:$REMOTE_DIR"
echo "Copied latest 'execute_commands_on_azure.sh' file from local machine to Azure VM"

# Copy generated jar file to server
scp -i "$SSH_KEY" target/brighthr-0.0.1-SNAPSHOT.jar "$AZURE_INSTANCE:$REMOTE_DIR"
echo "Copied jar file from local machine to Azure VM"

# Copy Liquibase schema file to server (if needed)
scp -i "$SSH_KEY" dbchangelog-4.6.xsd "$AZURE_INSTANCE:$REMOTE_DIR"
echo "Copied Liquibase schema file from local machine to Azure VM"

# Connect to Azure VM and start server using execute_commands_on_azure.sh
ssh -i "$SSH_KEY" "$AZURE_INSTANCE" "cd $REMOTE_DIR && ./execute_commands_on_azure.sh"
echo "Connected to Azure VM and started server using execute_commands_on_azure.sh"
