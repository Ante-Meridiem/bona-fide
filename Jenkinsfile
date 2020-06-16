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
    	stage('Build Docker Image'){
		sh 'docker build -f Dockerfile -t talk2linojoy/bona-fide-docker .'
	}
	stage('Push Docker'){
		withCredentials([string(credentialsId: 'docker-hub-password', variable: 'dockerHubPassword')]) {
			sh "docker login -u talk2linojoy -p ${dockerHubPassword}"	
		}
	}
	stage('Run Docker Container'){
		sh 'docker run -d -p 9002:9002 talk2linojoy/bona-fide-docker'
	}
}
