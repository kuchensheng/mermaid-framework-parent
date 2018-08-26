pipeline {
    agent any
    parameters {
<#list parameterMap?keys as key>
            string name:'${key}',defaultValue:'',description:'${parameterMap[key]}'
</#list>
    }
    <#if deployUrl??>
        environment {
            DEPLOY_URL = '${deployUrl}'
        }
    </#if>
    stages {
        stage('执行部署') {
            steps {
                echo 'play submit'
                deployWithParam(${input})
                {
                    timeout(${timeout}) {
                        script {
                            def qg = waitDeploy()
                            def result = "${result}"
                            if(result != 'SUCCESS') {
                                sh 'exit 1'
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            sh "curl -X POST --connect-timeout 10 --header 'Content-Type:application/json' --header 'Accept:application/json' '${callbackUrl}${parameter}' > /dev/null 2>&1 &"
        }
    }
}