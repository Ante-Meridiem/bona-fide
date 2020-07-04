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
	
	stage('STOPPING CURRENT RUNNING CONTAINER'){
		sh 'docker stop bona_fide_container'
		sh 'docker rename bona_fide_container bona_fide_container_old
	}
	
	stage('DOCKER CONTAINER RUN'){
		sh "docker run -d -p 9002:9002 --name bona_fide_container talk2linojoy/${buildVersion}"
	}
	
	stage('DOCKER CONTAINER HEALTH CHECK'){
		script {
                    final String url = 'http://ec2-13-235-2-41.ap-south-1.compute.amazonaws.com:9002/home'
                    final String response = sh(script: "curl -Is $url | head -1", returnStdout: true).trim()
			if(response == "HTTP/1.1 200"){
				sh 'docker rm bona_fide_container_old'
			}
			else{
				sh 'docker rm bona_fide_container'
			}
                }
	}
	
}

def getBuildVersion(){
	git credentialsId: 'bona-fide', url: 'git@github.com:Ante-Meridiem/Bona-Fide.git'
	def masterCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
	def currentDate = sh(returnStdout: true, script: 'date +%Y-%m-%d').trim()
	env.BUILD_VERSION = currentDate + "-" + masterCommit
}
