# wejuai-accounts

帐号和用户系统

### 帐号系统
支持多种类型账号登录：
- 微信
- qq
- 微博

可以跨平台登录：
- web
- 小程序
- 公众号
- 手机网页

其中微信必须有unionId功能

### 第三方登录注册流程介绍

1. 前端直接跳转后台构建的`/login/{平台}`系列接口
2. 用户点了授权后跳转到`/authorize/{平台}`系列回调接口
3. 在回调接口查询帐号记录，查到了就直接添加到session中。如果查不到就进入注册流程
4. 创建绑定信息对象存储到session中，然后跳转到注册页
5. 调用注册接口时从session获取之前存储的信息进行注册，然后删除存储的信息加入账号信息。

### 外部关联
- aliyun mns
- aliyun oss
- qq互联
- 微博开放平台
- 微信开放平台
- 微信公众平台
- wejuai-core

### 额外结构
- domain：对于mail所需的实体
- infrastructure：功能基础支持
- `emailCode.html`为发送邮件的模版
- `resources/static`内的网页为登录模块，可以在任何其他同域名下加载注册弹窗

### 配置项
- 详情参考`wejuai-config-server`中的配置文件
- `bootstrap.yml`中的config-server配置
- `build.gradle`中的github或者其他获得dto和entity以及工具包的仓库

### 本地运行
1. 配置项以及其中的第三方服务开通
   gradle build，其中github的仓库必须使用key才可以下载，需要在个人文件夹下的.gradle/gradle.properties中添加对应的key=value方式配置，如果不行，就去下载对应仓库的代码本地install一下
2. 启动配置项中的数据库
3. 分别运行`Application.java`的`main()`方法

### docker build以及运行
- 运行gradle中的docker build task
- 如果配置了其中的第三方仓库可以运行docker push，会先build再push
- 运行方式 docker run {image name:tag}，默认是运行的profile为dev，可以通过环境变量的方式修改，默认启动配置参数在Dockerfile中