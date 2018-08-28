stage('${type}') {
            steps {
                echo 'checkout'
                git credentialsId: '${credentialsId}',url: '${gitUrl}', branch: '${branch}'
            }
            post {
                always {
                    echo 'checkout callback'
                    ${post_script!}
                }
            }
        }