### 小鸡库存监控
 （一）软件运行要求 <br>
 1. jdk 1.8 以上
 2. maven 3.8.6
 3. 内存 256m 以上

 （二）打包 <br>
`mvn clean package '-Dmaven.test.skip=true'`

 （三）配置文件 <br>
 1. monitor.properties 需要配置参数以及位置配置到run.sh shell脚本中。
 2. message.* 消息相关参数是配置钉钉消息推送，需要在pc客户端配置群机器人得到参数。
 3. message.enable=0 时不推送钉钉消息
 4. proxy.enable=0 时不启动网络代理
 
 （四）运行 <br>
 1. 修改run.sh 中参数 <br>
    **DIRECTORY**  jar包配置文件存放位置，二者尽量存在在一个目录。<br>
    **JAR_FILE_NAME**  jar包名称，需与打包后的jar包保持一致。否则无法启动。
 2. sh run.sh 运行

 （五）运行前准备<br>
 1. java --version 检查java环境
 2. mvn --version 检查maven打包环境