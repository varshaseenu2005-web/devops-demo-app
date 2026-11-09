// Declarative Jenkins pipeline: Maven build -> test -> package -> Ansible deploy
pipeline {
    agent any

    tools {
        maven 'Maven3'   // Manage Jenkins > Tools > Maven installations (name: Maven3)
        jdk   'JDK17'    // Manage Jenkins > Tools > JDK installations   (name: JDK17)
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build') {
            steps { sh 'mvn -B clean compile' }
        }

        stage('Test') {
            steps { sh 'mvn -B test' }
            post {
                always { junit 'target/surefire-reports/*.xml' }
            }
        }

        stage('Package') {
            steps { sh 'mvn -B package -DskipTests' }
            post {
                success { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true }
            }
        }

        stage('Deploy with Ansible') {
            steps {
                // Uses ansible/inventory.ini (localhost by default; edit for real servers)
                sh '''
                    ansible-playbook -i ansible/inventory.ini ansible/site.yml \
                      -e "jar_src=${WORKSPACE}/target/devops-demo-app.jar"
                '''
            }
        }

        stage('Smoke Test') {
            steps { sh 'sleep 5 && curl -fsS http://localhost:8080/health' }
        }
    }

    post {
        success { echo 'Pipeline succeeded.' }
        failure { echo 'Pipeline failed. Check the console output.' }
    }
}
