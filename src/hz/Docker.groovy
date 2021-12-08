package hz

/**
* @author simahao
*
*/
class Docker implements Serializable {

    private final def script

    Docker(def script) {
        this.script = script
    }

    public void login() {
        this.login(${Constants.DOCKER_USER}, ${Constants.DOCK_PWD}, ${Constants.DOCKER_REGESTRY_DOMAIN})
    }

    public void login(String user, String pwd, String dockerRegistryDomain) {
        this.script.sh("docker login -u ${user} -p ${pwd} ${dockerRegistryDomain}")
    }

    public void buildDockerImage(String imageName) {
        this.buildDockerImage(imageName, ${git.getHeadHash()})
    }

    public void buildDockerImage(String imageName, String tag) {
        this.buildDockerImage(imageName, tag, ${Constants.DOCKER_REGESTRY_DOMAIN})
    }

    public void buildDockerImage(String imageName, String tag, String domain) {
        def git = new Git(this.script)
        this.script.sh("docker build -t ${domain}/${imageName}:${tag} .")
    }

    public void publishDockerImageToN3(String imageName) {
        this.publishDockerImageToN3(imageName, ${git.getHeadHash()})
    }

    public void publishDockerImageToN3(String imageName, String tag) {
        this.publishDockerImageToN3(imageName, tag, ${Constants.DOCKER_REGESTRY_DOMAIN})
    }

    public void publishDockerImageToN3(String imageName, String tag, String domain) {
        def git = new Git(this.script)
        this.script.sh("docker push ${domain}/${imageName}:${tag}")
    }
}
