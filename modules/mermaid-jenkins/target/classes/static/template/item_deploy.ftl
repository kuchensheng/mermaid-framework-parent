stage('${type}') {
            steps {
                echo 'building'
                script {
                    def settings = download('${settingsUrl}','${jobName}')
<#if tools?? &&(tools?size > 0)>
    <#assign str="">
    <#list tools as item>
        def ${item.toolsName} = tool '${item.toolsVersion}'
        <#assign str = str +"${r'${'}${item.toolsName}"+"}/bin:">
    </#list>
    env.PATH = "${str}${r'${env.PATH}'}"
</#if>

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
            post {
                always {
                    echo 'build callback'
                    ${post_script!}
                }
            }
        }