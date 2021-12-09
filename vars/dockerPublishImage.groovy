import hz.Docker
import hz.Constants

/**
 * Usage:
 *
 * publishImage imageName: microservice-java, tag: v1.0.0, regestryDomain: dev2.docker
 */
def call(Map args, Closure body={}) {
    def docker = new Docker(this)
    node {
        stage("Publish Docker Image to N3") {
            def user = Constants.DOCKER_USER
            def pwd = Constants.DOCKER_PWD
            def regestryDomain = Constants.DOCKER_REGESTRY_DOMAIN
            if (${args.user} != "") {
                user = "${args.user}"
            }
            if (${args.pwd} != "") {
                pwd = "${args.pwd}"
            }
            if (${args.registryDomain} != "") {
                regestryDomain = "${args.registryDomain}"
            }
            docker.login(user, pwd, registryDomain)
            docker.publishImage2N3(user, pwd, regestryDomain)
        }
        body()
    }
}
