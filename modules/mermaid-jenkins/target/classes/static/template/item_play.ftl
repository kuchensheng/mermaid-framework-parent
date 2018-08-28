stage('${type}') {
            steps {
                echo 'play submit'
                script{
                        def pom = readMavenPom file: params.pomPath
                        def groupId = pom.getGroupId()
                        def artifactId = pom.getArtifactId()
                        def version = pom.getVersion()
                        if(null == groupId || null == version) {
                            def parent = pom.getParent()
                            groupId = parent.getGroupId()
                            version = parent.getVersion()
                        }
                        def params = '&group='+groupId+'&name='+artifactId+'&version='+version
                        def input = sh returnStdout:true,script:"curl -X POST --connect-timeout 10 --header 'Content-Type:application/json' --header 'Accept:application/json' '${deployUrl}${playUrl}?jobName=${r'${env.JOB_NAME}'}&buildNumber=${r'${env.BUILD_NUMBER}'}${r'${params}'}'"
                        def propsResult = readJSON text:input
                        def code = propsResult['code']
                        echo "code="+code
                        if(code != 'success') {
                            sh 'exit 1'
                        }
                        if(null == propsResult['data']){
                            echo '[ERROR] pkgUrl is null'
                            sh 'exit 1'
                        }
                        execDeploy(propsResult['data'],artifactId,version)
            }
                    }
            post {
                always {
                    echo 'play callback'
                    ${post_script!}
                }
            }
        }