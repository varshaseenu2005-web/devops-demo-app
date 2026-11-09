# Automated Software Build and Configuration Management
### Using Maven, Jenkins, Ansible and Azure Pipelines

A small Java 17 web app plus the full automation around it:

| Tool | Role | File |
|------|------|------|
| Maven | Build, test, package into a runnable JAR | `pom.xml` |
| Jenkins | CI/CD pipeline (build, test, package, deploy) | `Jenkinsfile`, `jenkins/`, `docker-compose.yml` |
| Ansible | Configuration management and deployment | `ansible/` |
| Azure Pipelines | Cloud CI/CD (build, publish artifact, Ansible deploy) | `azure-pipelines.yml` |

## Flow
```
Git push -> Jenkins / Azure Pipelines -> Maven (compile, test, package)
         -> Ansible (install Java, create user, copy JAR, start app)
         -> Smoke test (GET /health)
```

## Requirements
- JDK 17+ and Maven 3.8+  (`java -version`, `mvn -version`)
- Ansible (Linux / WSL / macOS): `sudo apt install ansible`
- Docker (optional, only for running Jenkins locally)

## Quick start

### Option 1: Maven only
```bash
bash scripts/run-simple.sh
# open http://localhost:8080  and  http://localhost:8080/health
```
Or manually: `mvn clean package` then `java -jar target/devops-demo-app.jar`

### Option 2: Maven + Ansible (configuration management)
```bash
bash scripts/run-local.sh
```
Ansible installs Java, creates `appuser`, copies the JAR to `/opt/devops-demo-app`, starts it and waits for port 8080.
Re-running it shows `ok` instead of `changed` (idempotency).

### Option 3: Jenkins
```bash
docker compose up -d --build
docker logs devops-jenkins        # copy the initial admin password
```
1. Open http://localhost:9090, install suggested plugins.
2. Manage Jenkins > Tools: add JDK named `JDK17` and Maven named `Maven3`
   (or tick "Install automatically").
3. New Item > Pipeline > "Pipeline script from SCM" > your Git repo of this project
   (or paste the `Jenkinsfile` contents into "Pipeline script").
4. Build Now. Stages: Checkout, Build, Test, Package, Deploy with Ansible, Smoke Test.

### Option 4: Azure Pipelines
1. Push this project to GitHub or Azure Repos.
2. Azure DevOps > Pipelines > New pipeline > select repo > "Existing Azure Pipelines YAML file" > `/azure-pipelines.yml`.
3. Run. Stages: Build and Test (Maven), Deploy (Ansible on the agent).

## Ansible on a real server
Edit `ansible/inventory.ini`:
```
[app_servers]
192.168.1.10 ansible_user=ubuntu ansible_ssh_private_key_file=~/.ssh/id_rsa
```
Then:
```bash
ansible-playbook -i ansible/inventory.ini ansible/site.yml
```
(`run_as_service=true` by default, which installs a systemd service.)

## Project structure
```
devops-project/
  pom.xml
  Jenkinsfile
  azure-pipelines.yml
  docker-compose.yml
  jenkins/Dockerfile
  src/main/java/com/devops/app/App.java
  src/test/java/com/devops/app/AppTest.java
  ansible/
    ansible.cfg  inventory.ini  site.yml
    roles/app/{defaults,tasks,templates,handlers}
  scripts/run-simple.sh  scripts/run-local.sh
```

## Troubleshooting
- `mvn: command not found`: install Maven and add it to PATH.
- Port 8080 busy: `APP_PORT=9000 java -jar target/devops-demo-app.jar`.
- Ansible on Windows: use WSL (Ansible does not run natively on Windows).
- Ansible apt tasks need a Debian/Ubuntu target.
