pipeline {
  agent {
    node {
      label 'mavenjdk17'
    }

  }
  stages {
    stage('拉取代码') {
      agent none
      steps {
        container('maven') {
          git(url: 'https://gitee.com/gyr679/gker-love-back.git', credentialsId: 'gitee', branch: 'master', changelog: true, poll: false)
          sh 'ls -lh'
        }

      }
    }

    stage('打包') {
      agent none
      steps {
        container('maven') {
          sh 'ls -lh'
          sh 'mvn -v'
          sh 'mvn clean package -Dmaven.test.skip=true'
        }

      }
    }

    stage('构建镜像') {
      agent none
      steps {
        container('maven') {
          sh 'ls -lh'
          sh 'docker --version'
          sh 'docker build -t gkerlove-back:latest .'
        }

      }
    }

    stage('推送镜像') {
      agent none
      steps {
        container('maven') {
          withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,passwordVariable : 'DOCKER_PASSWD' ,usernameVariable : 'DOCKER_USER' ,)]) {
            sh 'echo "$DOCKER_PASSWD" | docker login $REGISTRY -u "$DOCKER_USER" --password-stdin'
            sh 'docker tag gkerlove-back:latest $REGISTRY/$DOCKERHUB_NAMESPACE/gkerlove-back:SNAPSHOT-$BUILD_NUMBER'
            sh 'docker push $REGISTRY/$DOCKERHUB_NAMESPACE/gkerlove-back:SNAPSHOT-$BUILD_NUMBER'
          }

        }

      }
    }

    stage('部署到k8s') {
      agent none
      steps {
        container('maven') {
          withCredentials([kubeconfigFile(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
            sh 'envsubst < deploy/deploy.yaml | kubectl apply -f -'
          }

        }

      }
    }

    stage('部署成功邮件通知') {
      agent none
      steps {
        mail(to: '157679566@qq.com', subject: '部署结果', body: 'gkerlove-back构建成功！')
      }
    }

  }
  environment {
    REGISTRY = 'registry.cn-qingdao.aliyuncs.com'
    DOCKERHUB_NAMESPACE = 'gkerlove'
  }
}