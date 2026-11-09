#!/usr/bin/env bash
# Build with Maven, then deploy with Ansible (local machine).
set -euo pipefail
cd "$(dirname "$0")/.."

echo "==> 1/3 Building with Maven"
mvn -B clean package

echo "==> 2/3 Deploying with Ansible"
ansible-playbook -i ansible/inventory.ini ansible/site.yml \
  -e "jar_src=$(pwd)/target/devops-demo-app.jar" \
  -e "run_as_service=false" --ask-become-pass

echo "==> 3/3 Smoke test"
curl -fsS http://localhost:8080/health && echo
echo "Done. Open http://localhost:8080"
