pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean install'
            }
        }
        stage('Docker Build') {
          agent any
          steps {
            sh 'docker build -t fundrequestio/platform:${BRANCH_NAME} .'
          }
        }
    }
}