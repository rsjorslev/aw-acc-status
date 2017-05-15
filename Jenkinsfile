pipeline {
  agent any
  tools { 
    maven 'maven339'
  }
  stages {
    stage ('Echo Maven and Path') {
      steps {
        sh '''
            echo "PATH = ${PATH}"
            echo "M2_HOME = ${M2_HOME}"
        ''' 
      }
    }
    /*
    stage('Setup') {
      steps {
        sh 'env'
      }
    }
    */
    stage('Build') {
      steps {
        sh 'mvn -B -V -U -e clean install'
      }
    }
  }
  post {
    always {
      echo 'One way or another, I have finished'
      junit 'target/surefire-reports/**/*.xml'
      archiveArtifacts(artifacts: 'target/*.jar, target/classes/static/docs/*.html', onlyIfSuccessful: true, fingerprint: true)
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
