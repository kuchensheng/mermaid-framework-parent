# 关于mermaid-framework
**mermaid-framework**是用用程序核心框架，基于Spring boot开发。**mermaid-framework**包含以下组件：

+ [mermaid-framework-core](modules/mermaid-core/README.md) -> 应用核心框架、启动器
+ [mermaid-framework-redis](modules/mermaid-redis/README.md) -> Redis访问组件，支持缓存
分布式计数器、分布式锁
+ [mermaid-framework-plugin](modules/mermaid-plugin/README.md) -> 框架插件支撑
+ [mermaid-framework-websocket](modules/mermaid-websocket/README.md) -> WebSocket组件
+ [mermaid-framework-zuul](modules/mermaid-zuul/README.md) -> 网关组件
+ [mermaid-framework-sdk](modules/mermaid-sdk/README.md) -> 基础服务调用组件

## Quick Start
### pom.xml引用
```xml
<dependencies>
    <dependency>
        <groupId>com.mermaid.framework</groupId>
        <artifactId>mermaid-core</artifactId>
        <version>${mermaid.framework.version}</version>
    </dependency>
</dependencies>
```
### 概念转变
WEB应用部署方式从"WEB容器部署war应用"变为"从JAVA应用程序中嵌入式WEB SERVER",一个容器中部署多个不同应用的时代已经过去，新一代JAVAWEB的应用核心是
**轻量级**，核心技术是嵌入式WEB SERVER（TOMCAT、JETTY）等。
推荐项目打成jar包并以JAVA应用程序的方式去构建，部署和启动WEB应用
### 使用maven插件打包
```xml
<build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <excludes>
                    <exclude>**/*.java</exclude>
                </excludes>
                <includes>
                    <include>**/*.*</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.*</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
        <!--<pluginManagement> -->
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>1.3.5.RELEASE</version>
                <configuration>
                    <mainClass>com.mermaid.framework.MermaidApplicationEntry</mainClass>
                    <layout>JAR</layout>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>

    </build>
```

**命令：** `mvn clean package`,${maven-project}/target目录下将会生成jar包${maven-project}-${version}.jar

## 启动
**命令**:`java -jar ${maven-project}-${version}.jar`,自定义端口访问，例如：http://localhost:8080

**Copyright © 库陈胜**