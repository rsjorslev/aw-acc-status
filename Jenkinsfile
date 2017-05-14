pipeline {
  agent {
    docker {
      image 'maven:3.3.9'
    }
    
  }
  stages {
    stage('Unit Tests') {
      steps {
        sh 'mvn clean test'
      }
    }
  }
}