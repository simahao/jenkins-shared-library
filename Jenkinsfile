// pipeline {
//     agent any
//     stages {
//         stage('generate toml') {
//             steps {
//                 script {
//                     def version
//                     if (env.BRANCH_NAME ==~ 'b-.*') {
//                         version = env.BRANCH_NAME
//                     } else if (env.BRANCH_NAME ==~ '[sS]anpshot') {
//                         version = "snapshot"
//                     } else if (env.TAG_NAME ==~ /v\d+\.\d+\.\d+/) {
//                         version = env.TAG_NAME
//                     } else {
//                         echo "no changes.."
//                         sh 'exit 0'
//                     }
//                     echo "$version"
//                 }
//             }
//         }
//     }
// }
// pipeline {
//     agent any
//     stages {
//         stage("test") {
//             steps {
//                 timeout(time: 1, unit: 'MINUTES') {
//                 script {
//                     env.DEPLOY_ENV = input message: '选择部署的环境', ok: 'deploy',
//                         parameters: [choice(name: 'DEPLOY_ENV', choices: ['prd', 'uat', 'test'], description: '选择部署环境')]

//                         switch("${env.DEPLOY_ENV}"){
//                             case 'prd':
//                                 println('deploy prd env')
//                                 break;

//                             case 'uat':
//                                 println('deploy uat env')
//                                 break;

//                             case 'test':
//                                 println('deploy test env')
//                                 break;

//                             default:
//                                 println('error env')

//                         }
//                     }
//                 }
//             }
//         }
//     }
// }
// pipeline {
//     agent any
//     stages {
//         stage("test") {
//             steps {
//                 script {
//                     Map cfg = readYaml file: "pipeline.yml"
//                     echo "${cfg.compile.exec}"
//                 }
//             }
//         }
//     }
// }

@Library("hz-shared") _

runPipeline()
