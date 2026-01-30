#!/bin/bash

# SSH Tunnel Script for PostgreSQL Database Connection
# This script creates an SSH tunnel to access the remote PostgreSQL database

# SSH Configuration (Update these with your actual SSH credentials)
SSH_HOST="139.84.210.248.vultrusercontent.com"  # SSH server hostname or IP
SSH_USER="root"                                   # SSH username (update this)
SSH_PORT="22"                                     # SSH port (usually 22)
SSH_KEY_PATH=""                                   # Path to SSH private key (optional, leave empty for password auth)

# Database Configuration (on remote server)
DB_HOST="localhost"                               # Database host on remote server (usually localhost)
DB_PORT="5432"                                   # PostgreSQL port on remote server

# Local Tunnel Configuration
LOCAL_PORT="5433"                                 # Local port for tunnel (use different from 5432 to avoid conflicts)

echo "=========================================="
echo "SSH Tunnel for PostgreSQL Database"
echo "=========================================="
echo "SSH Host: $SSH_HOST"
echo "SSH User: $SSH_USER"
echo "Remote DB: $DB_HOST:$DB_PORT"
echo "Local Port: $LOCAL_PORT"
echo ""

# Check if tunnel is already running
if lsof -Pi :$LOCAL_PORT -sTCP:LISTEN -t >/dev/null 2>&1 ; then
    echo "⚠️  Port $LOCAL_PORT is already in use!"
    echo "   Another tunnel might be running, or you need to use a different port."
    echo ""
    read -p "Kill existing process on port $LOCAL_PORT? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        kill $(lsof -t -i:$LOCAL_PORT) 2>/dev/null
        sleep 2
    else
        echo "Exiting. Please free port $LOCAL_PORT or update LOCAL_PORT in this script."
        exit 1
    fi
fi

# Build SSH command
if [ -z "$SSH_KEY_PATH" ]; then
    # Password authentication
    echo "Creating SSH tunnel (password authentication)..."
    echo "You will be prompted for SSH password."
    SSH_CMD="ssh -N -L $LOCAL_PORT:$DB_HOST:$DB_PORT $SSH_USER@$SSH_HOST -p $SSH_PORT"
else
    # Key-based authentication
    echo "Creating SSH tunnel (key-based authentication)..."
    SSH_CMD="ssh -N -L $LOCAL_PORT:$DB_HOST:$DB_PORT -i $SSH_KEY_PATH $SSH_USER@$SSH_HOST -p $SSH_PORT"
fi

echo ""
echo "Starting SSH tunnel..."
echo "Command: $SSH_CMD"
echo ""
echo "⚠️  Keep this terminal open while using the application!"
echo "   Press Ctrl+C to close the tunnel when done."
echo ""
echo "=========================================="

# Start the tunnel
$SSH_CMD

# If tunnel closes
echo ""
echo "SSH tunnel closed."
