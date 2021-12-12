/**
* @author simahao
*
*/
def call(Closure body={}) {
    node{
        //script pipeline should checkout mannual
        //declared pipeline checkout automatically
        checkout(
            changelog: true,
            poll: true,
            scm: [$class: 'GitSCM',
                branches: scm.branches,
                extensions: scm.extensions + [[$class: 'CleanBeforeCheckout']],
                userRemoteConfigs: scm.userRemoteConfigs
            ]
        )
        Map cfg = readYaml file: "pipeline.yml"

        switch(cfg.type) {
            case "java":
                javaPipeline(cfg, body)
                break
            case "javascript":
                javascriptPipeline(cfg, body)
                break
            case "docker":
                dockerPipeline(cfg, body)
                break
            case "python":
                pythonPipeline(cfg, body)
                break
            case "nodejs":
                nodejsPipeline(cfg, body)
                break
            defalut:
                log.error("language:${cfg.type} is not supported in this environment")
                break
        }
    }
}
