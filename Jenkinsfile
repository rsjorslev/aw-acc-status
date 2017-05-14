pipeline {
  agent {
    docker {
      image 'maven:3.3.9'
    }
    
  }
  stages {
    stage('Unit Tests') {
      steps {
        parallel(
          "Unit Tests": {
            sh 'mvn clean test'
            
          },
          "Environment": {
            sh 'echo ${env}'
            
          }
        )
      }
    }
  }
  post {
    always {
      junit 'target/surefire-reports/**/*.xml'
      archive 'target/**/*.jar'
      
    }
    
  }
}