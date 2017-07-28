pipeline {
  agent any
<<<<<<< HEAD
  stages {
    stage('Echo Maven and Path') {
      steps {
        sh '''
            echo "PATH = ${PATH}"
            echo "M2_HOME = ${M2_HOME}"
        '''
        sh '''env > env.txt
cat env.txt'''
      }
    }
    stage('Echo credentials') {
=======
  tools {
    maven 'maven339'
  }
  stages {
    stage('Setup') {
>>>>>>> origin/master
      steps {
        sh '''echo "this is a test to show creds"
echo "these are the creds ${CREDS}"
echo "user ${CREDS_USR}"
echo "pass ${CREDS_PSW}"
echo "foo: ${FOO}"
echo "env ${env}"'''
        echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
      }
    }
<<<<<<< HEAD
=======
    stage('Unit Tests') {
      steps {
        sh 'mvn -B -V -U -e clean test'
      }
    }
>>>>>>> origin/master
    stage('Build') {
      steps {
        sh 'mvn -B -V -U -e clean install'
      }
<<<<<<< HEAD
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
    
=======
    }   
    stage ('Artifactory Deploy') {
      steps {
        script {
          def server = Artifactory.server('artifactory01')
          def rtMaven = Artifactory.newMavenBuild()
          rtMaven.resolver server: server, releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot'
          rtMaven.deployer server: server, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
          rtMaven.tool = 'maven339'
          def buildInfo = rtMaven.run pom: 'pom.xml', goals: 'install'
          server.publishBuildInfo buildInfo
        }
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
>>>>>>> origin/master
  }
}
