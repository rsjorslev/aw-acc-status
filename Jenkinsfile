pipeline {
		agent any
        node {
                def server = Artifactory.server "artifactory521"
                def rtMaven = Artifactory.newMavenBuild()
        }
		tools {
				maven 'maven339'
		}
		stages {
				stage('Init') {
						steps {
						sh '''
						echo "PATH = ${PATH}"
						echo "M2_HOME = ${M2_HOME}"
						'''
						}
				}
				stage('Build') {
						steps {
									sh 'mvn install'
							}
				}
				stage('Test') {
						steps {
								echo 'test stage'
						}
						post {
								success {
										junit 'target/surefire-reports/**/*.xml' 
								}
						}
				}
				stage('Deploy') {
						steps {
								echo 'deploy'
						}
				}
		}
}
