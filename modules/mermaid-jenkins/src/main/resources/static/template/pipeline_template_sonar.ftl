pipeline {
    agent any
<#if cron??>
        triggers {
        cron('${cron}')
        }
</#if>


    stages {
        <#if cron??>
        stage('Advise') {
            steps {
                echo 'advise business schedule is executing...'
                sh "curl -X POST --connect-timeout 10 --header 'Content-Type:application/json' --header 'Accept:application/json' '${callbackUrl}${beforeCallBack}'"
            }
        }
        </#if>
        stage('checkout') {
            steps {
                echo 'checkout'
                git credentialsId: '${credentialsId}',url: '${gitUrl}', branch: '${branch}'
            }
        }
        stage('SONAR') {
            steps {
                echo 'sonaring...'
                script {
                    scannerHome = tool '${scannerName}'
                }
                withSonarQubeEnv('sonar6.7.1') {
                    sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${projectKey} -Dsonar.sources=./ -Dsonar.java.binaries=./"
                }
                 script {
                timeout(10) {
                    def qg = waitForQualityGate()
                    echo "Sonarqube result : ${result}"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'I will call back'
            sh "curl -X POST --connect-timeout 10 --header 'Content-Type:application/json' --header 'Accept:application/json' '${callbackUrl}${parameter}' > /dev/null 2>&1 &"
        }
    }

}