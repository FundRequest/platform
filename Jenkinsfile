pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        timeout(time: 15, unit: 'MINUTES')
    }
    stages {
        def platform
        def adminPanel
        stage('Build') {
            steps {
                sh 'mvn -B clean install'
            }
        }
        stage('Docker Build') {
          steps {
            platform = docker.build("fundrequestio/platform")
            adminPanel = docker.build("fundrequestio/adminweb")
          }
        }
        stage('Docker Push') {
          steps {
              docker.withRegistry('https://registry.hub.docker.com', 'dockerHub') {
                  platform.push("${BRANCH_NAME}")
                  adminPanel.push("${BRANCH_NAME}")
              }
          }
        }
    }
}