package hz

/**
* @author simahao
*
*/
class Docker implements Serializable {

    private final def script

    static final String dockerUser = "docker"
    static final String dockerPwd = "Dev2_docker"
    static final String dockerRegistryUrl = "dev2.docker"

    Docker(def script) {
        this.script = script
    }

    void login() {
        this.script.sh("""docker login -u ${dockerUser} -p ${dockerPwd} ${dockerRegistryUrl}""")
    }
    void login(String registry, String user, String pwd) {
        this.script.sh("""docker login -u ${user} -p ${pwd} ${registry}""")
    }

    void buildDockerImage(String microserviceName) {
        def git = new Git(this.script)
        script.sh("docker build -t ${dockerRegistryIdentifier}/${microserviceName}:${git.getHeadHash()} .")
    }

    void publishDockerImageToN3(String microserviceName) {
        loginToAWSECRDockerRegistry(1)

        def git = new Git(this.script)
        script.sh("docker push ${dockerRegistryIdentifier}/${microserviceName}:${git.getHeadHash()}")
    }
}
