script {
                    def callResult = sh returnStdout:true,script:"curl -X POST --connect-timeout 10 --header 'Content-Type:application/json' --header 'Accept:application/json' '${callbackUrl}${parameter}&type=${type}'"
                    def props = readJSON text:callResult
                    def code = props['code']
                    echo "code="+code
                    if(code != 'success') {
                        sh 'exit 1'
                        }
                    }