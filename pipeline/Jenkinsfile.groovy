#!groovy

pipeline {
    agent any
    parameters {
            string(name: 'REPO_OWNER', defaultValue: 'Akansh09', description: 'Git Repo Owner?')
            string(name: 'REPO_NAME', defaultValue: 'sonar-analysis', description: 'Git Repo Name?')
            string(name: 'SONAR_PROJECT', defaultValue: 'sonar-analysis', description: 'Sonar Project?')
            string(name: 'TARGET_BRANCH', defaultValue: 'main', description: 'Target branch?')
            string(name: 'SONAR_KEY', defaultValue: 'Akansh09_sonar-analysis_15fb42fe-8cb3-459f-86ea-7eb5b2e2db21', description: 'Sonar Key?')
    }

    triggers {
        pollSCM('*/5 * * * *')
    }

    stages {
     stage('SonarQube Analysis') {
       steps {
           script {
              def gitCommitHash = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
              sh "$MAVEN_HOME/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=${params.SONAR_KEY} -Dsonar.projectName=${params.SONAR_PROJECT} -Dsonar.host.url=$SONARQUBE_URL -Dsonar.token=$SONARQUBE_LOGIN -Dsonar.projectVersion=$gitCommitHash"
           }
        }
      }
      stage('Post SonarQube Analysis') {
             steps {
             script {
                 sleep(time:120,unit:"SECONDS")
                     sh "$MAVEN_HOME/bin/mvn clean compile exec:java -Dexec.mainClass=\"com.sonar.analysis.SonarAnalysis\" -DSONAR_KEY=${params.SONAR_KEY} -DTARGET_BRANCH=${params.TARGET_BRANCH} -DGIT_REPO_OWNER=${params.REPO_OWNER} -DGIT_REPO_NAME=${params.REPO_NAME}"
                 }
             }
      }
   }
}