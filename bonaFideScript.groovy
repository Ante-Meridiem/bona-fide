def getReleaseConfirmation() {
    def inputMessage = "Please provide the release version for Bona Fide"
    getBuildVersion()
    timeout(time: 30, unit: 'MINUTES') {
        buildVersion = input(id: 'buildVersion', message: inputMessage, parameters: [
                [$class: 'TextParameterDefinition', defaultValue: env.BUILD_VERSION, description: 'Build Version', name: 'Release Version']])
    }
}

def getBuildVersion() {
    git credentialsId: 'bona-fide', url: 'git@github.com:Ante-Meridiem/Bona-Fide.git'
    def masterCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
    def currentDate = sh(returnStdout: true, script: 'date +%Y-%m-%d').trim()
    env.BUILD_VERSION = currentDate + "-" + masterCommit
}

def fecthJarAndDockerFile() {
    def fetchErrorMessage = 'Error while fetching the Jar and dockerfile'
    try {
        sh label: 'createTargetDirectory', script: '''sudo mkdir -p target'''
        sh label: 'copyJarFile', script: '''sudo cp /home/ec2-user/Bona-Fide/bona-fide.jar target'''
        sh label: 'copyDockerFile', script: 'sudo cp /home/ec2-user/Bona-Fide/Dockerfile .'
    }
    catch (Exception e) {
        error "${fetchErrorMessage} ${e.getMessage()}"
    }
}

def buildDockerImage() {
    def dockerImgBuildError = 'Error while creating docker image'
    try {
        sh "docker build -f Dockerfile -t docker4bonafide/bona-fide/${buildVersion} ."
    }
    catch (Exception e) {
        error "${dockerImgBuildError} ${e.getMessage()}"
    }
}

def pushDockerImage() {
    def dockerImagePushError = 'Error while pushing docker image'
    withCredentials([string(credentialsId: 'docker-hub-password', variable: 'dockerHubPassword')]) {
			sh "docker login -u talk2linojoy -p ${dockerHubPassword}"	
		}
    try {
        sh "docker push talk2linojoy/bona-fide/${buildVersion}"
    }
    catch (Exception e) {
        error "${dockerImagePushError} ${e.getMessage()}"
    }
}

def stopRunningContainer(){
    echo 'Hi'
}

def runContainer(){
    def dockerContainerRunError = 'Error while running the container'
    sshagent(['bonaFideDeploymentAccess']) {
        try{
            sh "docker run -d -p 9002:9002 --name bona_fide_container talk2linojoy/bona-fide/${buildVersion}"
        }
        catch(Exception e){
            error "${dockerContainerRunError} ${e.getMessage()}"
        }
        echo 'Waiting for a minute...'
        sleep 59
    }
}

def performHealthCheck(){
    def httpResponseStatus = "HTTP/1.1 200"
    final String url = 'http://ec2-13-126-97-24.ap-south-1.compute.amazonaws.com:9002/bona-fide/base/version'
    final String response = sh(script: "curl -Is $url | head -1", returnStdout: true).trim()
    if(response == "${httpResponseStatus}"){
        APPLICATION_RUNNING_STATUS = true
    }
}

return this
