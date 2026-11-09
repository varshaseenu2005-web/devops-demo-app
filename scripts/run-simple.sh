#!/usr/bin/env bash
# Quickest run (Maven only, no Ansible): build and start the app.
set -euo pipefail
cd "$(dirname "$0")/.."
mvn -B clean package
java -jar target/devops-demo-app.jar
