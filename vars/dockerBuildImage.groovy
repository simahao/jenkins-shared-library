import hz.Docker
import hz.Constants

def call(Map args, Closure body={}) {
    node {
        def docker = new Docker(this)
        stage("Build Docker Image") {
            if (${args.imageName} == "") {
                log.error("you should add imageName parameter")
                sh "exit 1"
            }
            def tag = docker.getHeadHash()
            def regestryDomain = Constants.DOCKER_REGESTRY_DOMAIN
            log.debug("${args.tag}")
            log.debug("${args.regestryDomain}")
            if (${args.tag} != "") {
                tag = ${args.tag}
            }
            if (${args.regestryDomain} != "") {
                regestryDomain = ${args.regestryDomain}
            }
            docker.buildImage(args.imageName, tag, regestryDomain)
        }
        body()
    }
}
