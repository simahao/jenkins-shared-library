import hz.Docker
import hz.Git
import hz.Constants

/**
* @author simahao
* This class support stream format
*
*/
def call(Map args=[:]) {
    node {
        stage("Checkout") {
        //    new Git(this).checkout(args.repo)
            log.info("multibranch pipeline, checkout automatically")
        }
    }
    return this
}

def compile(Map args) {
    node {
        stage("Compile") {
            sh "${args.command}"
        }
    }
    return this
}

def unitTest(Map args) {
    node {
        stage("Unit Test") {
            sh "${args.command}"
        }
    }
    return this
}

def qualityAnalyze(Map args) {
    node {
        stage("Quality Analyze") {
            sh "${args.command}"
        }
    }
    return this
}

// def package(Map args) {
//     node {
//         stage("Package Artifact"){
//             sh "${args.command}"
//         }
//     }
//     return this
// }

def buildImage(Map args) {
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
    }
    return this
}

def publishImage(Map args) {
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
    }
    return this
}

def additionalPostBuildSteps(Closure body={}) {
    node {
        body()
    }
}
