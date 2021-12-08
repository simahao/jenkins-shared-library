import hz.Docker

/**
 * Usage:
 *
 * publishDockerImage awsCliVersion: 1, imageName: microservice-java
 */
def call(Map args, Closure body={}) {
    def Docker = new Docker(this)
    node {
        stage("Publish Docker Image to ECR") {
            Docker.loginToAWSECRDockerRegistry(args.awsCliVersion)
            Docker.publishDockerImageToN3(args.imageName)
        }
        body()
    }
}
