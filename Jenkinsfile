pipeline {
  agent any
  stages {
    stage('Unit Tests') {
      steps {
        sh 'mvn -B -V -U -e clean test'
      }
    }
  }
}