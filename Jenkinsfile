node{
	stage('Building'){
		def mvnHome = tool name: 'Maven', type: 'maven'
		def mvnCMD = "${mvnHome}/bin/mvn"
		sh "${mvnCMD} clean package"
	}
	
	stage('Build Docker Image){
		sh 'sudo docker build -t bona-fide-docker .'
	}
}