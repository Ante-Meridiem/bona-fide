def getReleaseConfirmation() {
    def inputMessage = "Please provide the release version for Bona Fide"
    getBuildVersion()
    timeout(time: 30, unit: 'MINUTES') {
        buildVersion = input(id: 'buildVersion', message: inputMessage, parameters: [
                [$class: 'TextParameterDefinition', defaultValue: env.BUILD_VERSION, description: 'Build Version', name: 'Release Version']])
    }
}

def getBuildVersion(){
    git credentialsId: 'bona-fide', url: 'git@github.com:Ante-Meridiem/Bona-Fide.git'
    def masterCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
    def currentDate = sh(returnStdout: true, script: 'date +%Y-%m-%d').trim()
    env.BUILD_VERSION = currentDate + "-" + masterCommit
}

def fecthJarAndDockerFile(){
    def fetchErrorMessage = 'Error while fetching the Jar and dockerfile'
    try{
        sh label: 'createTargetDirectory', script: '''sudo mkdir -p target'''
        sh label: 'copyJarFile', script: '''sudo cp /home/ec2-user/Bona-Fide/bona-fide.jar target'''
        sh label: 'copyDockerFile', script: 'sudo cp /home/ec2-user/Bona-Fide/Dockerfile .'
    }
    catch(Exception e){
        echo "${fetchErrorMessage} ${e.getMessage()}"
    }
}

def buildDockerImage() {
    def dockerImgBuildError = 'Error while creating docker image'
    try {
        sh "docker build -f Dockerfile -t docker4bonafide/bona-fide/${buildVersion} ."
    }
    catch (Exception e) {
        echo "${dockerImgBuildError} ${e.getMessage()}"
    }
}

def pushDockerImage(){
    withCredentials([string(credentialsId: 'docker-hub-password-bona-fide', variable: 'bonaFideDockerHubPassword')]) {
        sh "docker login -u docker4bonafide -p ${bonaFideDockerHubPassword}"
    }
}

return this