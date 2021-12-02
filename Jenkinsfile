@Library('pipeline-library') _

pipeline {
    agent {
        label 'ec2-jdk8'
    }

    triggers {
        cron('H 23 * * 1-5')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '1'))
        timeout(time: 20, activity: true)
    }

    stages {
        stage('Build Javadocs') {
            steps {
                echo 'Building Grid Javadocs ...'
                script {
                    withMaven() {
                        sh 'mvn clean javadoc:javadoc -f pom.xml'
                    }
                }
            }
        }
    }

    post {
        always {
            javadoc javadocDir: 'target/site/apidocs/', keepAll: false
        }

        failure {
            script {
                slack.sendFailureMessage()
            }
        }

        aborted {
            script {
                slack.sendInactivityMessage(20)
            }
        }
    }
}