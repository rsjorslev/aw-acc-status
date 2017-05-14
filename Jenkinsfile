pipeline {
  agent any
  stages {
    stage('Setup') {
      steps {
        sh 'env'
      }
    }
    stage('Maven info') {
      steps {
        sh 'mvn --version'
      }
    }
  }
  post {
    always {
      echo 'One way or another, I have finished'
      //junit 'target/surefire-reports/**/*.xml'
      //archive 'target/**/*.jar'
      //deleteDir() /* clean up our workspace */
    }
    success {
        echo 'I succeeeded!'
    }
    unstable {
        echo 'I am unstable :/'
    }
    failure {
        echo 'I failed :('
    }
    changed {
        echo 'Things were different before...'
    }
  }
}
