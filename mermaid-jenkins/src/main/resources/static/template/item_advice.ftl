stage('${type}') {
            steps {
                echo 'advise business schedule is executing...'
                ${post_script!}
            }
        }