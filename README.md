# springx-boot

JFormDesigner Swing 开发插件[下载](https://quqi.gblhgk.com/s/3367855/Qc9DsnJtw1gLkGL4), 查阅码 ZLwsk0

swing [基本教程](https://www.w3cschool.cn/java/codetag-swing.html)

## 使用教程

1）加入maven依赖

```xml
<dependency>
    <groupId>com.github.kancyframework</groupId>
    <artifactId>springx-boot-starter-gui</artifactId>
    <version>0.0.2-RELEASE</version>
</dependency>
```

2）启动类

```java
@SpringBootApplication
public class Application implements SwingSpringApplication<DemoFrame> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

3）事件监听

继承`AbstractActionApplicationListener` , 加入spring容器, 自动实现事件绑定：`@Action`
```java
@Component
@KeyStroke("ctrl shift alt F2")
@Action(value = "系统属性")
public class DemoListener extends AbstractActionApplicationListener<ActionApplicationEvent<DemoFrame>> {
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<DemoFrame> event) {
        // do some thing
    }
}
```

4）指定快捷键

事件监听类，使用注解：`@KeyStroke`