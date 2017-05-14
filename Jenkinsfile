pipeline {
  agent {
    docker {
      image 'maven'
      args '3.3.9'
    }
    
  }
  stages {
    stage('Unit Tests') {
      steps {
        sh 'mvn --version'
      }
    }
  }
}