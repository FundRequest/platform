node {
    def platform
    def adminPanel
    stage('Build') {
        sh 'mvn -B clean install'
    }
    stage('Docker Build') {
        platform = docker.build("fundrequestio/platform", "./tweb")
        adminPanel = docker.build("fundrequestio/adminweb", "./admin-web")
    }
    stage('Docker Push') {
      docker.withRegistry('https://registry.hub.docker.com', 'dockerHub') {
          platform.push("${BRANCH_NAME}")
          adminPanel.push("${BRANCH_NAME}")
      }
    }
}