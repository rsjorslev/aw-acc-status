pipeline {
  agent any
  stages {
    stage('Initialize') {
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
        sh 'mvn clean install -Dmaven.test.skip=true'
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
  tools {
    maven 'maven339'
  }
}