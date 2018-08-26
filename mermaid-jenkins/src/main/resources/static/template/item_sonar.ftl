stage('${type}') {
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
            post {
                always {
                    echo 'sonar callback'
                    ${post_script!}
                }
            }
        }