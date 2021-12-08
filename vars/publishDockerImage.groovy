import hz.Docker

/**
 * Usage:
 *
 * publishDockerImage awsCliVersion: 1, microserviceName: microservice-java
 */
def call(Map args, Closure body={}) {
    def Docker = new Docker(this)
    node {
        stage("Publish Docker Image to ECR") {
            Docker.loginToAWSECRDockerRegistry(args.awsCliVersion)
            Docker.publishDockerImageToECR(args.microserviceName)
        }
        body()
    }
}
