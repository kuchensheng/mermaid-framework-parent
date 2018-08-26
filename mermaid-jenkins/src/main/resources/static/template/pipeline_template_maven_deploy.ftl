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
                echo 'checkout'
                git credentialsId: '${credentialsId}',url: '${gitUrl}', branch: '${branch}'
            }
        }

        stage('Deploy') {
            steps {
                echo 'building'
                script {
                    def settings = download('${settingsUrl}','${jobName}')
                    def cmd = '${mvnDeployCmd} '
                    <#if deployType == "1">
                        echo 'build release'
                        sh cmd
                        sh '${mvnCleanCmd}'
                        sh '${mvnPrepareCmd}'
                        def stage = 'mvn -s '+settings
                        <#if mvnCmdParameters??>
                            stage = stage + '${mvnCmdParameters!}'
                        </#if>
                        stage = stage +' ${mvnStageCmd} -DstagingRepository=${repositoryId}::default::${repositoryUrl}'
                        sh stage
                    <#else>
                        echo 'build snapshot'
                        cmd = cmd + '-s '+settings
                        <#if mvnCmdParameters??>
                            cmd = cmd + '${mvnCmdParameters!}'
                        </#if>
                        cmd = cmd + ' -DaltDeploymentRepository=${repositoryId}::default::${repositoryUrl} '
                        sh cmd
                    </#if>
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
def download(settingsUrl,jobName) {
    echo 'downloding...'
    if(settingsUrl != "" && jobName != ""){
        def reName = jobName+".xml"
        sh 'rm -f ' +reName
        sh 'wget -c -p -t 3 -T 10 ' +settingsUrl +  ' -O '  + reName
        return './'+reName
    }
    return ""
}