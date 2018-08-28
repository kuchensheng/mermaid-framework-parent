def download(settingsUrl,jobName) {
    echo 'downloding...'
    if(settingsUrl != "" && jobName != ""){
        def reName = jobName+".xml"
        sh 'rm -f ' +reName
        sh 'wget -c -p -t 3 -T 10 ' +settingsUrl +  ' -O '  + reName
        return './'+reName
    }
    return ""
}