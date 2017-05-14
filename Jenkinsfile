pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh 'env'
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