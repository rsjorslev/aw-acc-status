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
    stage('Create Docker Image') {
      steps {
        script {
          docker.withRegistry('https://artifactory01.tstdmn.dk:5001', 'artifactory-admin') {
            def newApp = docker.build "aw-acc-status:${env.BUILD_TAG}"
            newApp.push()
            //docker.build('aw-acc-status:${env.BUILD_TAG}').push('latest')
          }
        }
      }
    }
//    stage ('Artifactory Deploy') {
//      steps {
//        script {
//          def server = Artifactory.server('artifactory01')
//          def rtMaven = Artifactory.newMavenBuild()
//          rtMaven.resolver server: server, releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot'
//          rtMaven.deployer server: server, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
//          rtMaven.tool = 'maven339'
//          def buildInfo = rtMaven.run pom: 'pom.xml', goals: 'install'
//          server.publishBuildInfo buildInfo
//        }
//      }
//    }
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
