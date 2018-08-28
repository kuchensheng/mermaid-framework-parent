#mermaid-framework框架核心实现
## 1.Introduction
mermaid-framework-core是mermaid框架的核心，它目前已具有如下**组件和功能：**
+ 全局统一配置加载
+ 嵌入式Web容器
+ SpringMVC
+ Mybatis集成
+ Mybatis分页插件
+ DataSourceTransaction
+ Swagger2集成

它在未来**将会具有：**
+ HTTP请求全链路跟踪
+ 应用安全防护（XSS、CSRF）
+ 应用状态报告（Actuator）

## 2. QuickStart
```xml
<dependency>
    <groupId>com.mermaid.framework</groupId>
    <artifactId>mermaid-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```