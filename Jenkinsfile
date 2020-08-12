#!/usr/bin/env groovy

node {

    withEnv(['SPRING_PROFILES_ACTIVE=jenkins']) {

        stage('checkout') {
            checkout scm
        }

        stage('check java') {
            sh "java -version"
        }

        stage('clean') {
            sh "chmod +x mvnw"
            sh "./mvnw -ntp clean"
        }

        stage('checkstyle') {
            sh "./mvnw -ntp checkstyle:check"
        }

        stage('compile with tests') {
            try {
                sh "./mvnw -ntp verify"
            } catch (err) {
                throw err
            } finally {
                junit '**/target/test-results/**/TEST-*.xml'
            }
        }

        stage('packaging') {
            sh "./mvnw -ntp install -DskipTests -Pprod"
        }

//        stage('deploying app on azure') {
//            sh 'scp target/skispasse-*-SNAPSHOT.jar skispasse-azure-admin@13.82.88.209:/home/skispasse-azure-admin/'
//        }
    }
}
