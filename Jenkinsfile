node{
	/*stage('Scm Checkout'){
	   // git credentialsId: 'bona-fide', url: 'git@github.com:Ante-Meridiem/Bona-Fide.git'
	   git branch: "${COMMIT_HASH}", credentialsId: 'bona-fide', url: 'git@github.com:Ante-Meridiem/Bona-Fide.git'
	}*/
	stage('Mvn Package'){
	    def mvnHome = tool name: 'Maven', type: 'maven'
	    def mvnCmd = "${mvnHome}/bin/mvn"
	    sh "${mvnCmd} clean package"
	}
	stage('Release Confirmation'){
		def inputMessage = "Please Provide Release Version"
        	timeout(time: 30, unit: 'MINUTES') {
            		buildVersion = input(id: 'buildVersion', message: inputMessage, parameters: [
                    	[$class: 'TextParameterDefinition', defaultValue: '', description: 'buildVersion', name: 'desc']])
            		sh label: '', script: 'date +"%Y-%m-%d %T"'
			echo "BuildVersion: ${buildVersion}"
			echo "label: ${label}"
        	}
	}
    	stage('Build Docker Image'){
		//sh 'docker build -f Dockerfile -t talk2linojoy/bona-fide-docker .'
		sh "docker build -f Dockerfile -t talk2linojoy/${buildVersion} ."
	}
	stage('Push Docker Image'){
		withCredentials([string(credentialsId: 'docker-hub-password', variable: 'dockerHubPassword')]) {
			sh "docker login -u talk2linojoy -p ${dockerHubPassword}"	
		}
		//sh 'docker push talk2linojoy/bona-fide-docker'
		sh "docker push talk2linojoy/${buildVersion}"
	}
	stage('Run Docker Container'){
		//sh 'docker run -d -p 9002:9002 --name BonaFideContainer talk2linojoy/bona-fide-docker'
		sh "docker run -d -p 9002:9002 --name BonaFideContainer talk2linojoy/${buildVersion}"
	}
}
