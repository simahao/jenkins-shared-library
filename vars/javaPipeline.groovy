import hz.Git
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

def unitTests(Map args) {
    node {
        stage("Unit Tests") {
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

def package(Map args) {
    node {
        stage("Package Artifact") {
            sh "${args.command}"
        }
    }
    return this
}

def publish2N3(Map args) {
    node {
        stage("Publish to Nexus3") {
            log.info("Publishing to Nexus3")
            sh "${args.command}"
        }
    }
    return this
}

def postBuildSteps(Closure body={}) {
    node {
        body()
    }
}
