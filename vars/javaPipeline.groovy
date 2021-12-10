import hz.Git
import hz.Docker
import hz.Constants

def call(Map args, Closure body={}) {
    // node {
    try {
        if (args.compile.exec) {
            stage("Compile") {
                for (cmd in args.compile.cmds) {
                    sh "${cmd}"
                }
            }
        }

        if (args.unitTest.exec) {
            stage("Unit Test") {
                for (cmd in args.unitTest.cmds) {
                    sh "${cmd}"
                }
            }
        }

        // if (args.qualityAnalyze.exec) {
        //     stage("Quality Analyze") {
        //         withSonarQube(credentials: "${args.sonarCrendentials}", installationName: "${args.sonarInstallationName}") {
        //             for (cmd in args.qualityAnalyze.cmds) {
        //                 sh "${cmd}"
        //             }
        //         }
        //     }
        // }

        if (args.mvnInstall.exec) {
            stage("Install") {
                for (cmd in args.mvnInstall.cmds) {
                    sh "${cmd}"
                }
            }
        }

        if (args.mvnDeploy.exec) {
            stage("Deploy") {
                for (cmd in args.mvnDeploy.cmds) {
                    sh "${cmd}"
                }
            }
        }


        // if (args.jacocoReport) {
        //     stage("Jacoco Report") {
        //         jacoco()
        //     }
        // }

        // if (args.junitReport) {
        //     stage("Junit Report") {
        //         junit()
        //     }
        // }

        if (args.sshPublish.exec) {
            stage("Publish Artifacts To Remote") {
                for (transfer in args.sshPublish.transfers) {
                    sshPublisher(
                        failOnError: true,
                        publishers: [
                            sshPublisherDesc(
                                configName: "${args.sshPublish.configName}",
                                verbose: true,
                                transfers: [
                                    sshTransfer(
                                        sourceFiles: "${transfer.sourceFiles}",
                                        remoteDirectory: "${transfer.remoteDirectory}",
                                        removePrefix: "${transfer.removePrefix}",
                                        flatten: false,
                                        cleanRemote: "${transfer.cleanRemote}",
                                        execCommand: "${transfer.execCommand}",
                                        execTimeout: "${transfer.execTimeout}"
                                    )
                                ]
                            )
                        ]
                    )
                }
            }
        }

        // if (args.release.exec) {
        //     stage("Release version") {
        //         for (cmd in args.release.cmds) {
        //             sh "${cmd}"
        //         }
        //     }
        // }

        // if (args.buildDockerImage) {
        //     stage("Build Docker Image") {
        //         def docker = new Docker(this)
        //         if (${args.imageName} == "") {
        //             log.error("you should add imageName parameter")
        //             sh "exit 1"
        //         }
        //         def tag = docker.getHeadHash()
        //         def regestryDomain = Constants.DOCKER_REGESTRY_DOMAIN
        //         log.debug("${args.tag}")
        //         log.debug("${args.regestryDomain}")
        //         if (${args.tag} != "") {
        //             tag = ${args.tag}
        //         }
        //         if (${args.regestryDomain} != "") {
        //             regestryDomain = ${args.regestryDomain}
        //         }
        //         docker.buildImage(args.imageName, tag, regestryDomain)
        //     }
        // }

        // if (args.publishImage2N3) {
        //     stage("Publish Image to N3") {
        //         def docker = new Docker(this)
        //         def user = Constants.DOCKER_USER
        //         def pwd = Constants.DOCKER_PWD
        //         def regestryDomain = Constants.DOCKER_REGESTRY_DOMAIN
        //         if (${args.user} != "") {
        //             user = "${args.user}"
        //         }
        //         if (${args.pwd} != "") {
        //             pwd = "${args.pwd}"
        //         }
        //         if (${args.registryDomain} != "") {
        //             regestryDomain = "${args.registryDomain}"
        //         }
        //         docker.login(user, pwd, registryDomain)
        //         docker.publishImage2N3(user, pwd, regestryDomain)
        //     }
        // }

        body()
    } catch (e) {
        if (args.cleanWsAfterBuild) {
            echo "clean workspace after building"
            cleanWs()
        }
    }
}
