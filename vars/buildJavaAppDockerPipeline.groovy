import hz.Docker
import hz.Git

def call(Map args=[:]) {
    node {
        stage("Checkout") {
           new Git(this).checkout(args.repo)
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

def unitTests(Map args) {
    node {
        stage("Unit Tests"){
            sh "${args.command}"
        }
    }
    return this
}

def integrationTests(Map args) {
    node {
        stage("Integration Tests"){
            sh "${args.command}"
        }
    }
    return this
}

def packageArtifact(Map args) {
    node {
        stage("Package Artifact"){
            sh "${args.command}"
        }
    }
    return this
}

def buildDockerImage(Map args) {
    node {
        def Docker = new Docker(this)
        stage("Build Docker Image") {
            Docker.buildDockerImage("${args.imageName}")
        }
    }
    return this
}

def publishDockerImage(Map args) {
    node {
        def Docker = new Docker(this)
        stage("Publish Docker Image") {
            Docker.publishDockerImageToN3("${args.imageName}")
        }
    }
    return this
}

def additionalPostBuildSteps(Closure body={}) {
    node {
        body()
    }
}

