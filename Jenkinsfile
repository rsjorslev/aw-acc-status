pipeline {
  agent any
  stages {
    stage('Init') {
      steps {
        sh '''
              echo "PATH = ${PATH}"
              echo "M2_HOME = ${M2_HOME}"
          '''
      }
    }
    stage('Build') {
      steps {
        echo 'build'
        sh 'mvn clean install'
      }
    }
    stage('Test') {
      steps {
        echo 'test'
      }
      post {
        success {
          junit 'target/surefire-reports/**/*.xml' 
        }
      }
    }
    stage('Deploy') {
      steps {
        echo 'deploy'
      }
    }
  }
  tools {
    maven 'maven339'
  }
}
