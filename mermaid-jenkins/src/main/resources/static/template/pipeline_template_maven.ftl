${pipeline} {
    agent any
    tools {
//        maven apache-maven-3.5.0
//        jdk jdk-1.7.149
        <#list tools as item>
        ${item.toolsName} '${item.toolsVersion}'
        </#list>
    }

    stages(checkout) {
        steps {
            echo checkout
            git credentialsId: '${credentialsId}',url: '${gitUrl}', branch: '${branch}'
        }
    }

    stages(Deploy) {
        steps {
            echo building
            def settings = download(${settingsUrl},${jobName})
            def cmd = mvn clean deploy -Dmaven.skip.test=true 
            if(settings != null || settings != )
                cmd = cmd + " -s " + settings
            def repositoryId = '${repositoryId}'
            def repositoryUrl = '${repositoryUrl}'
            if(repositoryId !=  && repositoryUrl != ) {
                cmd = cmd + "-DaltDeploymentRepository="+repositoryId+"::default::"+repositoryUrl
            }
            cmd = cmd + '${cmdParameters}'
            echo cmd
            sh cmd
        }
    }
    post {
        always {
            echo I will call back 
            def callback= "curl -X POST --header Content-Type:application/json --header Accept:application/json '${callbackUrl}'
            sh callback
        }
    }

}
def download(settingsUrl,jobName) {
    echo downloding...
    def reName = jobName+".xml"
    sh rm -f +reName
    sh wget -c -p +settingsUrl +  -O  + reName
    return ./+reName
}