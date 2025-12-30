#!/bin/bash

# =========================
# CONFIG
# =========================
DOCKER_USERNAME=bipulk3335
DOCKER_TOKEN=dckr_pat_45os7KW8NwnMML7VJ48RY94ZrAg

NETWORK=app-network

BACKEND_IMAGE=bipulk3335/multi-car-repair
FRONTEND_IMAGE=bipulk3335/test-frontend

# =========================
# LOGIN USING TOKEN
# =========================
echo "$DOCKER_TOKEN" | docker login -u "$DOCKER_USERNAME" --password-stdin

# =========================
# CREATE NETWORK
# =========================
docker network inspect $NETWORK >/dev/null 2>&1 || \
docker network create $NETWORK

# =========================
# CLEAN OLD CONTAINERS
# =========================
docker rm -f backend frontend 2>/dev/null

# =========================
# PULL IMAGES
# =========================
docker pull $BACKEND_IMAGE
#docker pull $FRONTEND_IMAGE

# =========================
# RUN BACKEND
# =========================
docker run -d \
  --name backend \
  --network $NETWORK \
  -p 8080:8080 \
  $BACKEND_IMAGE

# =========================
# RUN FRONTEND
# =========================
# docker run -d \
#   --name frontend \
#   --network $NETWORK \
#   -p 3001:3001 \
#   -e VITE_API_BASE_URL=http://localhost:8080 \
#   $FRONTEND_IMAGE

#echo "✅ Frontend: http://localhost:3001"
echo "✅ Backend : http://localhost:8080"
