/**
* @author simahao
*
*/
def call(Closure body={}) {
    node{
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