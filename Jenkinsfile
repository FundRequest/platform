pipeline {
    agent any
    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3-jdk-8'
                }
            }
            steps {
                sh 'mvn -B clean install'
            }
        }
    }
}