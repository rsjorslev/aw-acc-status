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
  post {
    always {
      junit 'target/surefire-reports/**/*.xml',
      archive 'target/**/*.jar
    }
  }
}
