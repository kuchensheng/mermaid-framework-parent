import groovy.json.JsonSlurper
pipeline {
    agent any
    <#if parameterMap??>
        parameters {
        <#list parameterMap?keys as key>
            string name:'${key}',defaultValue:'',description:'${parameterMap[key]}'
        </#list>
}
    </#if>


${trigger!}

<#if deployUrl??>
        environment {
            DEPLOY_URL = '${deployUrl}'
        }
</#if>
    stages {
    <#list stages as stage>
        ${stage}
    </#list>
    }
    post {
        always {
                   sh "curl -X POST --connect-timeout 10 --header 'Content-Type:application/json' --header 'Accept:application/json' '${callbackUrl}${parameter}&type=${type}'"
                }
        }
}

${build_download!}
<#if parameterMap??>
def execDeploy(pkgUrl,artifactId,version) {
    deployWithParam("pkgUrl":pkgUrl,"module":artifactId,"version":version,"properties":"${r'${params.properties}'}")
                {
                    timeout(5) {
                        // script {
                            def qg = waitDeploy()
                            def result = "${r'${qg.result}'}"
                            echo "部署结果=${r'${result}'}"
                            if(result != 'SUCCESS') {
                                echo "部署失败，直接结束"
                                sh 'exit 1'
                            }
                        // }
                    }
                }
}
</#if>

