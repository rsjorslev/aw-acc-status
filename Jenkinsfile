pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        build 'test'
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