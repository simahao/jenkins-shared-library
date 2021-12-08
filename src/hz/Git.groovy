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
        this.script.git credentialsId: Constants.JENKINS_GITEA_CREDENTIALS_ID, url: "http://170.0.50.9:3002/${orgsOrUserName}/${repoName}.git"
    }

    String getHeadHash() {
        return this.script.sh(script: getLatestGitgetHeadHashCommand(), returnStdout: true).trim()
    }

    private static String getLatestGitgetHeadHashCommand() {
        return "git rev-parse HEAD"
    }
}
