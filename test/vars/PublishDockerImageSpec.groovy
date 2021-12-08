import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import hz.Docker

class PublishDockerImageSpec extends JenkinsPipelineSpecification {
    def publishDockerImage = null

    def setup(){
        publishDockerImage = loadPipelineScriptForTest("vars/publishDockerImage.groovy")
    }

    def "Test publish docker image"() {
        setup:
            def Docker = GroovyMock(Docker.class, global: true)
            new Docker(publishDockerImage) >> Docker
        when:
            publishDockerImage awsCliVersion: 1, microserviceName: "microservice-name"
        then:
            1 * Docker.loginToAWSECRDockerRegistry(1)
            1 * Docker.publishDockerImageToECR("microservice-name")
    }

    def "Test run closure block after publish docker image"() {
        setup:
            def body = Mock(Closure)
            def Docker = GroovyMock(Docker.class, global: true)
            new Docker(publishDockerImage) >> Docker
        when:
            publishDockerImage awsCliVersion: 1, microserviceName: "microservice-name", body
        then:
            1 * body()
    }
}