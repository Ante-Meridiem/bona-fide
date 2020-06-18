node{
	stage('CLEAN BUILD'){
	    def mvnHome = tool name: 'Maven', type: 'maven'
	    def mvnCmd = "${mvnHome}/bin/mvn"
	    sh "${mvnCmd} clean package"
	}
	
	stage('RELEASE CONFIRMATION'){
		def inputMessage = "Please provide the RELEASE VERSION"
		getBuildVersion()
        	timeout(time: 30, unit: 'MINUTES') {
            		buildVersion = input(id: 'buildVersion', message: inputMessage, parameters: [
                    	[$class: 'TextParameterDefinition', defaultValue: env.BUILD_VERSION , description: 'Build Version', name: 'Release Version']])
        	}
	}
	
    	stage('DOCKER IMAGE BUILD'){
		sh "docker build -f Dockerfile -t talk2linojoy/${buildVersion} ."
	}
	
	stage('DOCKER IMAGE PUSH'){
		withCredentials([string(credentialsId: 'docker-hub-password', variable: 'dockerHubPassword')]) {
			sh "docker login -u talk2linojoy -p ${dockerHubPassword}"	
		}
		sh "docker push talk2linojoy/${buildVersion}"
	}
	
	stage('DOCKER CONTAINER RUN'){
		sh "docker run -d -p 9002:9002 --name BFContainer/${buildVersion} talk2linojoy/${buildVersion}"
	}
}

def getBuildVersion(){
	git credentialsId: 'bona-fide', url: 'git@github.com:Ante-Meridiem/Bona-Fide.git'
	def masterCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
	def currentDate = sh(returnStdout: true, script: 'date +%Y-%m-%d').trim()
	env.BUILD_VERSION = currentDate + "-" + masterCommit
}
