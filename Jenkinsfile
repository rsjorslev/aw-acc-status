pipeline {
  agent {
    docker {
      image 'maven:3.3.3'
    }
    
  }
  stages {
    stage('Setup') {
      steps {
        sh 'mvn --version'
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -B -V -U -e clean install'
      }
    }
    stage('Unit Tests') {
      steps {
        sh 'mvn -B -V -U -e clean test'
      }
    }
  }
}