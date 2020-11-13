#### 项目简介：
TTCode是基于SpringBoot开发的代码生成小程序。此小程序并非微信小程序，是鄙人自命名后端小程序(不知道恰当否，哈哈哈)，可单独部署，也可以作为maven依赖无缝整合到自己的项目)

- 自定义模板
- 支持DDL语句解析SQL元数据

#### 单独部署：
- [体验](http://118.126.105.207:8080/code.html)

#### 小程序方式

##### 作为module引入自己的项目

修改pom.xml,删除spring-boot-maven-plugin插件配置
```xml
	<!--如果作为其他项目的依赖引入，请删掉此插件-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                    <addResources>true</addResources>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

#### 待完善：

- 支持连接数据源模式
- 界面交互优化
