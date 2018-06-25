pipeline {
    agent any
    environment {
        GITHUB_CREDS = credentials('GITHUB_CRED')
        CODECOV_TOKEN = credentials('PLATFORM_CODECOV_TOKEN')
    }
    options {
        disableConcurrentBuilds()
        timeout(time: 15, unit: 'MINUTES')
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -Dfeign.client.github.username=$GITHUB_CREDS_USR -Dfeign.client.github.password=$GITHUB_CREDS_PSW clean install'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco(
                          execPattern: '**/target/*.exec',
                          classPattern: '**/target/classes',
                          sourcePattern: '**/src/main/java',
                          exclusionPattern: '**/src/test*,**/*Exception*,**/*Config*'
                    )

                }
            }
        }
        stage('Reports') {
            steps {
                sh 'curl -s https://codecov.io/bash | bash -s -- -t $CODECOV_TOKEN'
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