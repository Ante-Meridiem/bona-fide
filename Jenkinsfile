pipeline{
  agent any
  stages{
    stage('BURN UP'){
      steps{
        script{
          groovy = load "bonaFideScript.groovy"
        }
      }
    }

    stage('PROCEED'){
      steps{
        script{
          groovy.getReleaseConfirmation()
        }
      }
    }

    stage('FETCH'){
      steps{
        script{
          groovy.fecthJarAndDockerFile()
        }
      }
    }

    stage('BUILD ROUSTER'){
      steps{
        script{
            groovy.buildDockerImage()
        }
      }
    }

    stage('PUSH ROUSTER'){
      steps{
        script{
          groovy.pushDockerImage()
        }
      }
    }

  }
}
