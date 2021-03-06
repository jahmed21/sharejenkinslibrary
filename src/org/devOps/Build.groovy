package org.devOps

def build() {
    tools.PrintMes("构建上传镜像","green")
    env.serviceName = "${JOB_NAME}".split("_")[0]
    env.dockerUrl = '39.100.108.229'
    env.newImageName = "${dockerUrl}/ali";

    withCredentials([usernamePassword(credentialsId: 'aliyun-registry-admin', passwordVariable: 'password', usernameVariable: 'username')]) {

       sh """
           docker login -u ${username} -p ${password}  ${dockerUrl}
           docker build -f ${dockerFiles} -t ${newImageName}:${tagName} .
           sleep 1
           docker push ${dockerFiles} -t ${newImageName}:${tagName}
           sleep 1
           docker rmi ${dockerFiles} -t ${newImageName}:${tagName}
        """
    }
}

// 打包
def tar(projectName, targetIp, credentialsId, tarName) {
    echo 'start tar ............'
    echo "start tar1 ${projectName}"
    echo "start tar2 ${targetIp}"
    echo "start tar3 ${credentialsId}"

    echo "start tar5 ${tarName}"

    sshagent(["${credentialsId}"]) {
        sh('ls -al')
        sh("rm -rf *.tar.gz && tar -zcvf ${tarName} ./* --exclude=./git")
        sh('ls -al')
//         sh """
//             ssh -o StrictHostKeyChecking=no -l root ${targetIp} uname -a
//             echo 123
//             ls -al
//             ssh -o StrictHostKeyChecking=no -l root ${targetIp} uname -a && pwd
//             ls -al
//             scp ${tarName} root@${targetIp}:${dir}
//             echo 456
//             ssh root@${targetIp} -tt "ls -al && cd ${dir} && mkdir -p ${projectName} && tar zxvf ${tarName} -C ${projectName}"
//         """
    }
}