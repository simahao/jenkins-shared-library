import hz.Docker

def call(Map args, Closure body={}) {
    node {
        def Docker = new Docker(this)
        stage("Build Docker Image") {
            Docker.buildDockerImage("${args.microserviceName}")
        }
        body()
    }
}
