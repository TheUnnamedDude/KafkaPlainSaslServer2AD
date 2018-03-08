#!/usr/binenv groovy

pipeline {
    agent any
    
    stages {
        stage('setup') {
            steps {
                sh './gradlew clean'
            }
        }

        stage('build') {
            steps {
                sh './gradlew build -x test'
            }
        }

        stage('test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('shadow jar') {
            steps {
                sh './gradlew shadowJar'
            }
        }

        stage('Publish artifacts') {
            steps {
                sh './gradlew publish'
            }
        }
    }

    post {
        always {
			junit '**/build/test-results/test/*.xml'
            deleteDir()
        }
    }
}