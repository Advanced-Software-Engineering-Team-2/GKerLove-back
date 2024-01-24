# GKerLove-back
果壳之恋后端项目

## 运行
启动SpringBoot项目即可。

## 配置

项目使用了配置中心，为了方便，也可以删除掉bootstrap.yml，创建application.yml作为项目的配置文件，在配置文件中需要配置：

- 邮箱服务器的密码
- redis的地址、端口、数据库编号、用户名、密码
- mongodb的地址、端口、用户名、密码、认证数据库、数据库
- 应用的JWT Secret，配置在JWT.appSecret键下
- 阿里云OSS的访问keyID、keySecret、roleArn，配置在OSS.accesssKeyId、accessKeySecret、roleArn键下

## 项目结构

标准的SpringBoot项目，数据库交互使用MongoTemplate。
