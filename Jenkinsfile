pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        timeout(time: 15, unit: 'MINUTES')
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean install'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
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
              sh "docker push fundrequestio/platform:${BRANCH_NAME} && docker push fundrequestio/adminweb:${BRANCH_NAME} && echo 'pushed'"
            }
          }
        }
    }
}