#!/bin/bash

DH_USERNAME=$1
DH_PASSWORD=$2

if [ -z "$DH_USERNAME" ] || [ -z "$DH_PASSWORD" ]
then
  echo "No username or password provided. Cannot initialize submission"
  echo "Usage: initialize_submission.sh <username> <password>"
  exit 1
fi

if ! command -v docker-compose &> /dev/null
then
  echo "Docker-compose is not installed. Cannot initialize submission"
  exit 1
fi

if ! command -v docker &> /dev/null
then
  echo "Docker is not installed. Cannot initialize submission"
  exit 1
fi

echo "Formatting compose file"
sed "s/USERNAME/$DH_USERNAME/g" ./docker-compose.yaml.tmpl > ./docker-compose.yaml

echo "Connecting to account $DH_USERNAME"
docker login -u "$DH_USERNAME" -p "$DH_PASSWORD"

echo "Using docker CLI"
echo "Building images for account $DH_USERNAME"
docker compose build
echo "Pushing images to Dockerhub account $DH_USERNAME"
docker compose push

echo "Submission initialized"
exit 0

