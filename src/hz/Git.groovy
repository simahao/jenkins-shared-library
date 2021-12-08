package hz

/**
* @author simahao
*
*/
class Git implements Serializable {

    private final def script

    Git(def script) {
        this.script = script
    }

    def checkout(String orgsOrUserName, String repoName) {
        this.script.git credentialsId: Constants.GITEA_CREDENTIALS_ID, url: "Constants.GITEA_URL/${orgsOrUserName}/${repoName}.git"
    }

    def checkout(String orgsOrUserName, String repoName, String giteaCredentail) {
        this.script.git credentialsId: ${giteaCredentail}, url: "Constants.GITEA_URL/${orgsOrUserName}/${repoName}.git"
    }    

    String getHeadHash() {
        return this.script.sh(script: "git rev-parse HEAD", returnStdout: true).trim()
    }
}
