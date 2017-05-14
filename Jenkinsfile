pipeline {
  agent {
    docker {
      image 'maven:3.3.9'
    }
    
  }
  stages {
    stage('Unit Tests') {
      steps {
        sh 'env'
      }
    }
  }
  post {
    always {
      junit 'target/surefire-reports/**/*.xml'
      archive 'target/**/*.jar'
      
    }
    
  }
}