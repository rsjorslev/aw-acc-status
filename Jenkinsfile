pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'build'
        tool 'maven339'
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