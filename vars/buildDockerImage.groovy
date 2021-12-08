import hz.Docker

def call(Map args, Closure body={}) {
    node {
        def docker = new Docker(this)
        stage("Build Docker Image") {
            if (${args.tag} == "") {
                docker.buildDockerImage("${args.imageName}")
            } else {
                docker.buildDockerImage("${args.imageName}", "${args.tag}")
            }
        }
        body()
    }
}
