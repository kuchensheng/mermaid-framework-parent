stage('${type}') {
            steps {
                echo 'unit...'
                script {


<#if tools?? &&(tools?size > 0)>
    <#assign str="">
    <#list tools as item>
        def ${item.toolsName} = tool '${item.toolsVersion}'

        <#assign str = str +"${r'${'}${item.toolsName}"+"}/bin:">
    </#list>
    env.PATH = "${str}${r'${env.PATH}'}"
</#if>
                    sh '${mvnCoberturaCmd!}${mvnCmdParameters!}'

                archiveArtifacts allowEmptyArchive: true, artifacts: '**/target/site/cobertura/coverage.xml', onlyIfSuccessful: true
                cleanWs deleteDirs: true, notFailBuild: true, patterns: [[pattern: '**/target', type: 'INCLUDE']]
                }

            }
            post {
                always {
                    echo 'unit callback'
                    ${post_script!}
                }
            }
        }