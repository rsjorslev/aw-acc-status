pipeline {
  agent any
  tools {
    maven 'maven339'
  }
  stages {
    stage('Setup') {
      steps {
        sh 'mvn --version'
      }
    }
    stage('Unit Tests') {
      steps {
        sh 'mvn -B -V -U -e clean test'
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -B -V -U -e clean install'
      }
    }
  }
  post {
    always {
      echo 'I always run'
    }    
    success {
      junit 'target/surefire-reports/**/*.xml'
      archiveArtifacts(artifacts: 'target/*.jar, target/classes/static/docs/*.html', onlyIfSuccessful: true, fingerprint: true)    
    }
    failure {
      echo 'I failed :('  
    }
    changed {
      echo 'Things were different before...'
    }
  }
}
