# DevOps End-to-End CI/CD Pipeline

A complete DevOps pipeline built on a Spring Boot REST service, covering Linux/Git fundamentals through full CI/CD automation on Azure.

## Stack
- **App**: Spring Boot 1.5.10 (Java 8), REST endpoint `/sayhi`
- **Build**: Maven
- **Infrastructure**: Terraform (Azure Resource Group, VNet, Subnet, NSG, VM, Storage Account)
- **Configuration**: Ansible (Java installation, deployment)
- **CI/CD**: Jenkins (Checkout → Build → Test → Archive → Deploy → Health Check)

## Live Deployment
- App running at: `http://<VM_PUBLIC_IP>:8080/sayhi?name=<yourname>`
- See `deployment-notes.txt` for current VM IP.

## Project Structure
```
├── src/                  # Spring Boot application source
├── terraform/            # Azure infrastructure as code
├── ansible/              # Server configuration & deployment playbook
├── Jenkinsfile           # CI/CD pipeline definition
└── pom.xml               # Maven build configuration
```

## Pipeline Stages
1. **Checkout** — pulls latest code from this branch
2. **Build** — `mvn clean package -DskipTests`
3. **Test** — runs unit tests (known JDK21/Spring Boot 1.5.10 compatibility issue with test context loading; documented, non-blocking)
4. **Archive** — archives the built JAR as a Jenkins artifact
5. **Deploy** — copies JAR to Azure VM via SSH and restarts the service
6. **Health Check** — verifies the deployed endpoint responds correctly

## Key Issues Solved
- Spring Boot 1.5.10 / cglib incompatibility with JDK 17+ (`--add-opens` for local runs; native Java 8 on the deployment VM)
- Azure Basic-SKU public IP deprecation and regional VM capacity limits (resolved by switching SKU and region)
- Ansible/Python 3.5 incompatibility with modern `ansible-core` on the target VM (deploy steps run via direct SSH)
- Jenkins `pkill` self-matching its own process during deploy (fixed with bracket-pattern trick)

## Setup
```bash
# Build
mvn clean package -DskipTests

# Provision infrastructure
cd terraform && terraform init && terraform apply

# Deploy
cd ../ansible && ansible-playbook -i environments/test/hosts playbooks/deploy.yml
```
