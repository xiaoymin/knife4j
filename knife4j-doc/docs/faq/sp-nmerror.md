# Springfox-Swagger升级到2.9.2导致的NoSuchMethodError异常

很多朋友在将SpringFox-Swagger版本升级到2.9.2版本后,运行程序都会出现如下错误：

```java
org.springframework.context.ApplicationContextException: Failed to start bean 'documentationPluginsBootstrapper'; nested exception is com.google.common.util.concurrent.ExecutionError: java.lang.NoSuchMethodError: com.google.common.collect.FluentIterable.concat(Ljava/lang/Iterable;Ljava/lang/Iterable;)Lcom/google/common/collect/FluentIterable;
	org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:176)
	org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:51)
	org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:346)
	org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:149)
	org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:112)
	org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:851)
	org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:540)
	org.springframework.web.servlet.FrameworkServlet.configureAndRefreshWebApplicationContext(FrameworkServlet.java:667)
	org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:633)
	org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:681)
	org.springframework.web.servlet.FrameworkServlet.initWebApplicationContext(FrameworkServlet.java:552)
	org.springframework.web.servlet.FrameworkServlet.initServletBean(FrameworkServlet.java:493)
	org.springframework.web.servlet.HttpServletBean.init(HttpServletBean.java:136)
	javax.servlet.GenericServlet.init(GenericServlet.java:158)
	org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:474)
	org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:79)
	org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:624)
	org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:349)
	org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:783)
	org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)
	org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:798)xxxxxxxxxx sp-nmerrororg.springframework.context.ApplicationContextException: Failed to start bean 'documentationPluginsBootstrapper'; nested exception is com.google.common.util.concurrent.ExecutionError: java.lang.NoSuchMethodError: com.google.common.collect.FluentIterable.concat(Ljava/lang/Iterable;Ljava/lang/Iterable;)Lcom/google/common/collect/FluentIterable;    org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:176)    org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:51)    org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:346)    org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:149)    org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:112)    org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:851)    org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:540)    org.springframework.web.servlet.FrameworkServlet.configureAndRefreshWebApplicationContext(FrameworkServlet.java:667)    org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:633)    org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:681)    org.springframework.web.servlet.FrameworkServlet.initWebApplicationContext(FrameworkServlet.java:552)    org.springframework.web.servlet.FrameworkServlet.initServletBean(FrameworkServlet.java:493)    org.springframework.web.servlet.HttpServletBean.init(HttpServletBean.java:136)    javax.servlet.GenericServlet.init(GenericServlet.java:158)    org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:474)    org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:79)    org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:624)    org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:349)    org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:783)    org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)    org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:798)java
```

造成该错误的原因是，Springfox-swagger引用的guava版本相对于你项目中版本较高,此时和低版本的guava引起冲突.

Springfox-Swagger中guava的版本为20

```xml
<dependency>
<groupId>com.google.guava</groupId>
<artifactId>guava</artifactId>
<version>20.0</version>
<scope>compile</scope>
</dependency>
```

解决此错误的办法是排除低版本的guava即可.

maven命令在项目根目录运行：`mvn dependency:tree`命令可以看到jar包项目的引用树结构，可以看到低版本的guava是那个jar引入的
 
 
 