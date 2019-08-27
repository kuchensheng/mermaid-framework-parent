pipeline {
    agent any
    environment {
        def credentialsId='19e1724c-1359-4510-b677-2d349794e14d'
    }


    stages {
        stage('checkout') {
            steps {
                echo 'checkout'
                git credentialsId: '${r'${credentialsId}'}',url: '${gitUrl}', branch: '${branch}'
            }
        }

        stage('替换配置'  ) {
              steps {
                sh '''
                  echo '替换配置'
                  cs="$config_path/$deploy_name"
                  if [ -d $cs ]; then
                      cd $project_module_path
                      cd "src/main/resources"
                      \\cp -r $cs/* .
                  fi
                '''
            }
        }

        stage('Deploy') {
            steps {
                echo 'building'
                script {
                    def cmd = '${mvnDeployCmd} '
                    def deployType = ${deployType}
                    <#if deployType == "1">
                        echo 'build release'
                        sh cmd
                        sh '${mvnCleanCmd}'
                        sh '${mvnPrepareCmd}'
                        def stage = 'mvn '
                        <#if mvnCmdParameters??>
                            stage = stage + ' ${mvnCmdParameters!}'
                        </#if>
                        stage = stage +' ${mvnStageCmd} -DstagingRepository=sxc-release::default::http://nexus.songxiaocai.com/repository/sxc-release/'
                        sh stage
                    <#else>
                        echo 'build snapshot'
                        <#if mvnCmdParameters??>
                            cmd = cmd + '${mvnCmdParameters!}'
                        </#if>
                        cmd = cmd + ' -DaltDeploymentRepository=sxc-snapshots::default::http://nexus.songxiaocai.com/repository/sxc-snapshots/ '
                        sh cmd
                    </#if>
                }
            }
        }

        stage('构建docker') {
            steps {
                echo 'build docker'
                sh '''
                    v=`cat build.v`
                    cd $project_module_path
                    if [ -f $config_path/$deploy_name/Dockerfile ]; then
                        \\cp $config_path/$deploy_name/Dockerfile .
                    fi
                    echo '\nRUN echo \'PINPOINT_NAME=\"${r'${deploy_name}'}\"\' >> /opt/pinpoint-env.sh' >> Dockerfile
                    build_env="$config_path/build_env"
                    if [ -f $build_env ]; then
                        build_env_value=`cat $build_env`
                        echo '\nRUN echo \'JAVA_OPTS=\"-Dspring.profiles.active=${r'${build_env_value}'}\"\' >> /opt/JAVA_OPTS.sh' >> Dockerfile
                    fi
                    docker build -t $hub_name/${r'${deploy_name}'}:$v .
                '''
            }
        }
        stage('发布到私服 ') {
            steps {
                echo 'deploy docker'
                withCredentials([usernamePassword(credentialsId:"$credentialsId_hub", passwordVariable: 'login_password', usernameVariable: 'login_name')]) {
                sh '''
                    v=`cat build.v`
                    docker login -u $login_name -p $login_password $hub_host
                    docker push $hub_name/${r'${deploy_name}'}:$v
                '''
                }
            }
        }
        stage('触发k8s') {
            steps {
                echo 'deploy k8s'
                sh '''
                    v=`cat build.v`
                    count=`kubectl get deploy ${r'${deploy_name}'} --namespace=$namespace|wc -l`
                    if [ $count == 2 ]
                        then
                        echo "exec update"
                        kubectl set image deployments/$deploy_name $deploy_name=$hub_name/${r'${deploy_name}'}:$v --namespace=$namespace
                    else
                        echo "exec deploy"
                        kubectl run ${r'${deploy_name}'} --image=$hub_name/${r'${deploy_name}'}:$v --replicas=$replicas --namespace=$namespace
                    fi
                '''
            }
        }
        stage('记录历史') {

            steps {
                sh '''
                    v=`cat build.v`
                    if [ -d $config_path/buildhistory ]; then
                        echo "$hub_name/${r'${deploy_name}'}:$v">>$config_path/buildhistory/${r'${deploy_name}'}
                    fi
                   '''
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
