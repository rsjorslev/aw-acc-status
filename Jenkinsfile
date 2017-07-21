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
    stage('Unit Tests') {
      steps {
        sh 'mvn -B -V -U -e clean test'
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
