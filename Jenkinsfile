pipeline {
  agent {
    docker {
      image 'maven'
      args '3.3.3'
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