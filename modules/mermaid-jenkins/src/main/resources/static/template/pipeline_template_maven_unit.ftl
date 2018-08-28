pipeline {
    agent any

    <#if cron??>
    triggers {
        cron('${cron}')
    }
    </#if>

    <#if tools?? &&(tools?size > 0)>
    tools {
        <#list tools as item>
        ${item.toolsName} '${item.toolsVersion}'
        </#list>
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
                echo 'checkout...'
                git credentialsId: '${credentialsId}',url: '${gitUrl}', branch: '${branch}'
            }
        }
        stage('unit') {
            steps {
                echo 'unit...'
                sh '${mvnCoberturaCmd}${mvnCmdParameters}'
                archiveArtifacts allowEmptyArchive: true, artifacts: '**/target/site/cobertura/coverage.xml', onlyIfSuccessful: true
                cleanWs deleteDirs: true, notFailBuild: true, patterns: [[pattern: '**/target', type: 'INCLUDE']]
            }
        }
    }

    post {
        always {
            sh "curl -X POST --connect-timeout 10 --header 'Content-Type:application/json' --header 'Accept:application/json' '${callbackUrl}${parameter}' > /dev/null 2>&1 &"
        }
    }

}