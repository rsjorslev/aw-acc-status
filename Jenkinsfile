pipeline {
  agent any
  tools { 
    maven 'maven339'
  }
  stages {
    stage ('Initialize') {
      steps {
        sh '''
            echo "PATH = ${PATH}"
            echo "M2_HOME = ${M2_HOME}"
        ''' 
      }
    }
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
