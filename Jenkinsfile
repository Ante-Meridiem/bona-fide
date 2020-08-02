node{
	//Ignoring the Build stage so as save the space
	/*stage('CLEAN BUILD'){
	    def mvnHome = tool name: 'Maven', type: 'maven'
	    def mvnCmd = "${mvnHome}/bin/mvn"
	    sh "${mvnCmd} clean package"
	}*/
	
	stage('RELEASE CONFIRMATION'){
		def inputMessage = "Please provide the RELEASE VERSION"
		getBuildVersion()
        	timeout(time: 30, unit: 'MINUTES') {
            		buildVersion = input(id: 'buildVersion', message: inputMessage, parameters: [
                    	[$class: 'TextParameterDefinition', defaultValue: env.BUILD_VERSION , description: 'Build Version', name: 'Release Version']])
        	}
	}
	
    	stage('DOCKER IMAGE BUILD'){
		sh "docker build -f Dockerfile -t talk2linojoy/bona-fide/${buildVersion} ."
	}
	
	stage('DOCKER IMAGE PUSH'){
		withCredentials([string(credentialsId: 'docker-hub-password', variable: 'dockerHubPassword')]) {
			sh "docker login -u talk2linojoy -p ${dockerHubPassword}"	
		}
		sh "docker push talk2linojoy/bona-fide/${buildVersion}"
	}
	
	stage('STOPPING RUNNING CONTAINER'){
		script{
			final String currentImageId = sh(script: 'docker ps -q -f name=bona_fide_container',returnStdout: true)
			if(currentImageId != null){
				isContainerAvailable = true
				echo 'Stopping Current Container'
				sh 'docker stop bona_fide_container'
				echo 'Stopping Container : bona_fide_container'
				echo 'Renaming Current Container'
				sh 'docker rename bona_fide_container bona_fide_container_old'
				echo 'Renamed bona_fide_container to bona_fide_container_old'
			}
		}	
	}
	
	stage('DOCKER CONTAINER RUN'){
		sh "docker run -d -p 9002:9002 --name bona_fide_container talk2linojoy/bona-fide/${buildVersion}"
		echo 'Waiting for a minute...' 
		sleep 59
	}
	
	stage('DOCKER CONTAINER HEALTH CHECK'){
		script {
                    final String url = 'http://ec2-13-235-2-41.ap-south-1.compute.amazonaws.com:9002/home'
                    final String response = sh(script: "curl -Is $url | head -1", returnStdout: true).trim()
			if(response == "HTTP/1.1 200"){
				final String dockerImageId = sh(script: 'docker ps -q -f name=bona_fide_container_old',returnStdout: true)  
				if(dockerImageId != null){
					sh 'docker rm bona_fide_container_old'
					echo 'Successfully removed the previous container' 
					echo "Deployment Successfull,Application Bona Fide is up and running in port 9002 with build version ${buildVersion}"
				}
			}
			else{
				echo 'Deployment Unsuccessfull!!!'
			}
                }
	}
	
}
def isContainerAvailable
def getBuildVersion(){
	git credentialsId: 'bona-fide', url: 'git@github.com:Ante-Meridiem/Bona-Fide.git'
	def masterCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
	def currentDate = sh(returnStdout: true, script: 'date +%Y-%m-%d').trim()
	env.BUILD_VERSION = currentDate + "-" + masterCommit
}
