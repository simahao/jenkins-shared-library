// echo "\033[30m 黑色字 \033[0m" 
// echo "\033[31m 红色字 \033[0m" 
// echo "\033[32m 绿色字 \033[0m" 
// echo "\033[\033m 黄色字 \033[0m" 
// echo "\033[34m 蓝色字 \033[0m" 
// echo "\033[35m 紫色字 \033[0m" 
// echo "\033[36m 天蓝字 \033[0m" 
// echo "\033[37m 白色字 \033[0m"
def trace(message) {
    ansiColor('xterm') {
        echo "\033[32m[TRACE]: ${message}\033[0m"
    }
}
def debug(message) {
    ansiColor('xterm') {
        echo "\033[32m[DEBUG]: ${message}\033[0m"
    }
}
def info(message) {
    ansiColor('xterm') {
        echo "\033[36m[INFO]: ${message}\033[0m"
    }
}

def warning(message) {
    ansiColor('xterm') {
        echo "\033[35m[WARN]: ${message}\033[0m"
    }
}

def error(message) {
    ansiColor('xterm') {
        echo "\033[35m[ERROR]: ${message}\033[0m"
    }
}

def fatal(message) {
    ansiColor('xterm') {
        echo "\033[35m[FATAL]: ${message}\033[0m"
    }
}
