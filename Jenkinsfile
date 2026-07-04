pipeline {
    agent any

    environment {
        VM_IP = "20.235.40.28"
        JAR_NAME = "devops-demo-0.3.0.jar"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test || true'
                junit allowEmptyResults: true, testResults: '**/target/surefire-reports/TEST-*.xml'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: "target/${JAR_NAME}", fingerprint: true
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'azure-vm-ssh', usernameVariable: 'SSH_USER', passwordVariable: 'SSH_PASS')]) {
                    sh '''
                        sshpass -p "$SSH_PASS" scp -o StrictHostKeyChecking=no target/${JAR_NAME} $SSH_USER@$VM_IP:/tmp/${JAR_NAME}
                        sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no $SSH_USER@$VM_IP "sudo pkill -f ${JAR_NAME} || true"
                        sleep 2
                        sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no $SSH_USER@$VM_IP "sudo mv /tmp/${JAR_NAME} /opt/app/${JAR_NAME}"
                        sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no $SSH_USER@$VM_IP "sudo bash -c 'nohup java -jar /opt/app/${JAR_NAME} > /opt/app/app.log 2>&1 & disown'"
                    '''
                }
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    sleep 10
                    curl -f "http://${VM_IP}:8080/sayhi?name=Jenkins" || exit 1
                '''
            }
        }
    }

    post {
        success {
            echo "Pipeline succeeded - app deployed and verified"
        }
        failure {
            echo "Pipeline failed - check stage logs above."
        }
    }
}
