pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean install'
            }
        }
        stage('Docker Build') {
          steps {
            sh 'docker build -t fundrequestio/platform:${BRANCH_NAME} tweb'
            sh 'docker build -t fundrequestio/adminweb:${BRANCH_NAME} admin-web'
          }
        }
        stage('Docker Push') {
          steps {
            withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
              sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
              sh "docker push fundrequestio/platform:${BRANCH_NAME}"
              sh "docker push fundrequestio/adminweb:${BRANCH_NAME}"
            }
          }
        }
    }
}