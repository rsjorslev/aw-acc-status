pipeline {
  agent any
  stages {
    stage('Echo Maven and Path') {
      steps {
        sh '''
            echo "PATH = ${PATH}"
            echo "M2_HOME = ${M2_HOME}"
        '''
      }
    }
    stage('Echo credentials') {
      steps {
        sh '''echo "this is a test to show creds"
echo "these are the creds ${CREDS}"
echo "user ${CREDS_USR}"
echo "pass ${CREDS_PSW}"
echo "foo: ${FOO}"
echo "env ${env}"'''
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -B -V -U -e clean install'
      }
    }
  }
  tools {
    maven 'maven339'
  }
  environment {
    CREDS = credentials('be90085a-9415-4dfb-b680-1103763952bc')
    FOO = 'bar'
  }
  post {
    always {
      echo 'One way or another, I have finished'
      junit 'target/surefire-reports/**/*.xml'
      archiveArtifacts(artifacts: 'target/*.jar, target/classes/static/docs/*.html', onlyIfSuccessful: true, fingerprint: true)
      
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