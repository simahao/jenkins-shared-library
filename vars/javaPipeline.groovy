import hz.Git
import hz.Docker
import hz.Constants

def call(Map args, Closure body={}) {

    try {
        // CHANGE_BRANCH is same as BRANCH_NAME except pr, BRANCH_NAME would be like PR-24
        // variable format ether "${args.branch}" or args.branch
        pattern = args.branch == null ? ".*" : "${args.branch}"

        // pattern match:
        // 1. variable ==~ "regex"
        // 2. variable ==~ /regex/
        // compile stage
        if (args.compile != null && args.compile.exec) {
            if (args.compile.branch != null) {
                pattern = "${args.compile.branch}"
            }
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("compile") {
                    log.info("preparing compile stage")
                    for (cmd in args.compile.cmds) {
                        sh "${cmd}"
                    }
                }
            } else {
                log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
            }
        } else {
            log.info("compile.exec is false,skip compile")
        }

        // unit test stage
        if (args.unitTest != null && args.unitTest.exec) {
            if (args.unitTest.branch != null) {
                pattern = "${args.unitTest.branch}"
            }
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("unit test") {
                    log.info("preparing unit test stage")
                    for (cmd in args.unitTest.cmds) {
                        sh "${cmd}"
                    }
                }
            } else {
                log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
            }
        } else {
            log.info("unitTest.exec is false,skip unit test")
        }

        // quality analyze stage
        // if (args.qualityAnalyze.exec) {
        //     stage("Quality Analyze") {
        //         withSonarQube(credentials: "${args.sonarCrendentials}", installationName: "${args.sonarInstallationName}") {
        //             for (cmd in args.qualityAnalyze.cmds) {
        //                 sh "${cmd}"
        //             }
        //         }
        //     }
        // }

        // mvn install stage
        if (args.mvnInstall != null && args.mvnInstall.exec) {
            if (args.mvnInstall.branch != null) {
                pattern = "${args.mvnInstall.branch}"
            }
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("mvn install") {
                    log.info("preparing mvn install stage")
                    for (cmd in args.mvnInstall.cmds) {
                        sh "${cmd}"
                    }
                }
            } else {
                log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
            }
        } else {
            log.info("mvnInstall.exec is false, skip mvn install")
        }

        // mvn deploy stage
        if (args.mvnDeploy != null && args.mvnDeploy.exec) {
            if (args.mvnDeploy.branch != null) {
                pattern = "${args.mvnDeploy.branch}"
            }
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("mvn deploy") {
                    log.info("preparing mvn deploy stage")
                    for (cmd in args.mvnDeploy.cmds) {
                        sh "${cmd}"
                    }
                }
            } else {
                log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
            }
        } else {
            log.info("mvnDeploy.exec is false, skip mvn deploy")
        }

        // jacoco report
        if (args.jacocoReport != null && args.jacocoReport.exec) {
            if (args.jacocoReport.branch != null) {
                pattern = "${args.jacocoReport.branch}"
            }
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("Jacoco Report") {
                    log.info("preparing jacoco report")
                    jacoco()
                }
            } else {
                log.info("jacocoReport.exec is false, skip jacoco report")
            }
        } else {
            log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
        }

        // junit report
        if (args.junitReport != null && args.junitReport.exec) {
            if (args.junitReport.branch != null) {
                pattern = "${args.jacocoReport.branch}"
            }
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("Junit Report") {
                    junit()
                }
            } else {
                log.info("junitReport.exec is false, skip junit report")
            }
        } else {
            log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
        }

        if (args.tagAndRelease != null && args.tagAndRelease.exec) {
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("tag and release to gitea") {
                    log.info("preparing tag and release to gitea stage")
                    withEnv(['PATH+EXTRA=/usr/local/node-v16.13.1/bin']) {
                        sh """
                            git fetch --all
                            git tag -l | grep -q "${args.tagAndRelease.tag}" && echo "tag:${args.tagAndRelease.tag} is exist,pls change tag name"
                            sed -E -i "s/(^.*version: +)(.*)/\1${args.tagAndRelease.tag}/g" package.json
                            ls CHANGELOG.md > /dev/null 2>&1 && line_b=\$(awk 'END{print NR}' CHANGELOG.md) || line_b=0
                            npm ls -g conventional-changelog-cli > /dev/null 2>&1 || npm install -g conventional-changelog-cli
                            npm run genlog
                            git config --local user.name "jenkins"
                            git config --local user.email "zhanghao@dce.com.cn"
                            git add .
                            git commit -m 'ci: changelog'
                            git tag "${args.tagAndRelease.tag}"
                            git push origin "${args.tagAndRelease.tag}"
                            line_a=\$(awk 'END{print NR}' CHANGELOG.md)
                            added=\$((\${line_a}-\${line_b}))
                            diff_log=\$(sed -n "1,\${added}p" CHANGELOG.md | awk -v d='\\\\n' '{s=(NR==1?s:s d)\$0}END{print s}')
                            token="b21ac3ee961836bdc642ae842a60e30bee901257"
                            gitea_url="http://wslhost:3000/api/v1/repos/${args.tagAndRelease.repo}/releases?token=\${token}"
                            curl -X 'POST' \
                                "\${gitea_url}" \
                                -H 'accept: application/json' \
                                -H 'Content-Type: application/json' \
                                -d "{
                                    \"body\": \"\$diff_log\",
                                    \"draft\": false,
                                    \"name\": \"${args.tagAndRelease.tag}\",
                                    \"prerelease\": true,
                                    \"tag_name\": \"${args.tagAndRelease.tag}\"
                                }"
                        """
                    }
                }
            } else {
                log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
            }
        } else {
            log.info("sss")
        }

        // continue deploy stage
        if (args.continueDeploy.branch != null) {
            pattern = "${args.continueDeploy.branch}"
        }
        if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
            stage("continue deploy") {
                //deploy different environment
                for (env in args.continueDeploy.env) {
                    if (env.exec) {
                        log.info("deploy env: ${env.name}")
                        //ssh artifact to remote servers in this environment
                        for (server in env.server) {
                            //ssh different atifacts(jar|*.yml...) to one server
                            log.info("server credential: ${server.configName}")
                            for (transfer in server.transfer) {
                                log.info("artifact: ${transfer.sourceFiles}")
                                sshPublisher(
                                    failOnError: true,
                                    publishers: [
                                        sshPublisherDesc(
                                            configName: "${server.configName}",
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
                    } else {
                        log.info("${env.name}.exec is false,skip ${env.name} deploy")
                    }
                }
            }
        } else {
            log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
        }

        // if (args.release != null && args.release.exec) {
        //     stage("Release version") {
        //         for (cmd in args.release.cmds) {
        //             sh "${cmd}"
        //         }
        //     }
        // }

        // build docker image
        if (args.docker.buildImage != null && args.docker.buildImage.exec) {
            if (args.docker.branch != null) {
                pattern = "${args.docker.branch}"
            }
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("build docker image") {
                    log.info("preparing build docker image")
                    def docker = new Docker(this)
                    if (args.docker.buildImage.imageName == null) {
                        log.error("you should add docker.buildImage.imageName parameter in pipeline.yml")
                        sh "exit 1"
                    }
                    imageTag = args.docker.imageTag == null ? docker.getDefaultImageTag() : args.docker.imageTag
                    domain = args.docker.domain == null ? Constants.DOCKER_REGESTRY_DOMAIN: args.docker.domain
                    log.debug("imageTag: ${imageTag}, regestry: ${regestry}, domain: ${domain}")
                    docker.buildImage(args.buildImage.imageName, imageTag, domain)
                }
            } else {
                log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
            }
        } else {
            log.info("docker.buildImage.exec is false, skip build docker image")
        }

        // publish image to Nexus3
        if (args.docker.publish2N3 != null && args.docker.publish2N3.exec) {
            if (args.docker.branch != null) {
                pattern = "${args.docker.branch}"
            }
            if (env.BRANCH_NAME ==~ pattern || env.CHANGE_BRANCH ==~ pattern || env.TAG_NAME ==~ pattern) {
                stage("publish image to n3") {
                    def docker = new Docker(this)
                    def user = Constants.DOCKER_USER
                    def pwd = Constants.DOCKER_PWD
                    domain = args.docker.domain == null ? Constants.DOCKER_REGESTRY_DOMAIN: args.docker.domain
                    docker.login(user, pwd, domain)
                    docker.publishImage2N3(user, pwd, domain)
                }
            } else {
                log.info("branch pattern: ${pattern}, branchName: ${env.BRANCH_NAME}, changeBranch: ${env.CHANGE_BRANCH}, tag: ${env.TAG_NAME}")
            }
        } else {
            log.info("docker.publish2N3.exec is false, skip publish image to N3")
        }
        body()
    } catch (e) {
        throw e
    } finally {
        if (args.delteWsAfterBuild) {
            log.info("delete workspace after building")
            cleanWs()
        }
    }
}
