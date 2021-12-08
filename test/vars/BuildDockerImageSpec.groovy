import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import hz.Docker

class BuildDockerImageSpec extends JenkinsPipelineSpecification {
    def buildDockerImage = null

    def setup() {
        buildDockerImage = loadPipelineScriptForTest("vars/buildDockerImage.groovy")
    }

    def "Test build Docker Image"() {
        setup:
            def Docker = GroovyMock(Docker.class, global: true)
            new Docker(buildDockerImage) >> Docker
            def body = Mock(Closure)
        when:
            buildDockerImage microserviceName: "microservice-name", body
        then:
            1 * Docker.buildDockerImage("microservice-name")
            1 * body()
    }
}
