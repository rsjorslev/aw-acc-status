pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'build'
        sh 'mvn --version'
      }
    }
    stage('Test') {
      steps {
        echo 'test'
      }
    }
    stage('Deploy') {
      steps {
        echo 'deploy'
      }
    }
  }
}