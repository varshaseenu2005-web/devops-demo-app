# Jenkins with Maven, JDK 17, Ansible and curl preinstalled
FROM jenkins/jenkins:lts-jdk17
USER root
RUN apt-get update && apt-get install -y maven ansible curl sudo \
    && echo "jenkins ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/jenkins \
    && rm -rf /var/lib/apt/lists/*
USER jenkins
