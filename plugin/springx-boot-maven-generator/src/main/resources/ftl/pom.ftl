<#noparse><?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.kancy</groupId></#noparse>
    <artifactId>${artifactId}</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>${artifactId}</name>
    <description>${artifactId}</description>
<#noparse>
    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>

        <springx.boot.version>0.0.4-RELEASE</springx.boot.version>
        <final.package.name>${artifactId}</final.package.name></#noparse>
        <startup.class.name>${packageName}.Application</startup.class.name><#noparse>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.github.kancyframework</groupId>
            <artifactId>springx-boot-starter-gui</artifactId>
            <version>${springx.boot.version}</version>
        </dependency>
        <dependency>
            <groupId>com.github.kancyframework</groupId>
            <artifactId>springx-boot-gui-miglayout</artifactId>
            <version>${springx.boot.version}</version>
        </dependency>
        <dependency>
            <groupId>com.github.kancyframework</groupId>
            <artifactId>springx-boot-minidb</artifactId>
            <version>${springx.boot.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <profiles>
        <profile>
            <id>javapackager</id>
            <properties>
                <profile.name>javapackager</profile.name>
            </properties>
            <activation>
                <activeByDefault>false</activeByDefault>
            </activation>
            <dependencies>
            </dependencies>
            <build>
                <plugins>
                    <!-- https://github.com/fvarrui/JavaPackager -->
                    <plugin>
                        <groupId>io.github.fvarrui</groupId>
                        <artifactId>javapackager</artifactId>
                        <version>1.7.0</version>
                        <configuration>
                            <!-- 名称与版本 -->
                            <displayName>${final.package.name}</displayName>
                            <name>${final.package.name}</name>
                            <version>${project.version}</version>

                            <!-- 启动类 -->
                            <mainClass>${startup.class.name}</mainClass>

                            <!-- 虚拟机参数 -->
                            <vmArgs>-Dtray=true -Dtray.console=true -Dtray.system.properties=true</vmArgs>

                            <!-- JRE 最低版本要求: 绑定jre时不生效
                            <jreMinVersion>8</jreMinVersion>
                            -->

                            <!-- 应用程序生成安装程序 -->
                            <generateInstaller>true</generateInstaller>

                            <!-- 输出目录（将生成工件的位置）-->
                            <outputDirectory>${project.basedir}/dist/${project.version}</outputDirectory>

                            <!-- 自定义绑定jre -->
                            <bundleJre>false</bundleJre>
                            <jrePath>${java.home}</jrePath>

                            <!-- 是否必须管理员权限-->
                            <administratorRequired>false</administratorRequired>
                            <!-- 复制依赖jar包-->
                            <copyDependencies>true</copyDependencies>
                        </configuration>
                        <executions>
                            <execution>
                                <id>bundling-for-windows</id>
                                <phase>package</phase>
                                <goals>
                                    <goal>package</goal>
                                </goals>
                                <configuration>
                                    <!-- 操作系统-->
                                    <platform>windows</platform>
                                    <!-- 打ZIP包-->
                                    <createZipball>true</createZipball>
                                    <!--详细参数配置 -->
                                    <winConfig>
                                        <!-- 图标文件 -->
                                        <icoFile>${project.basedir}/dist/icon.ico</icoFile>

                                        <!-- Generates Setup installer : 依赖innosetup -->
                                        <generateSetup>true</generateSetup>
                                        <generateMsi>false</generateMsi>

                                        <!-- 选择exe创建工具： launch4j, winrun4j or why -->
                                        <exeCreationTool>launch4j</exeCreationTool>

                                        <!-- EXE header type: console or gui-->
                                        <headerType>gui</headerType>

                                        <!-- setup generation properties-->
                                        <setupLanguages>
                                            <english>compiler:Default.isl</english>
                                        </setupLanguages>
                                        <!-- 安装语言 -->
                                        <language>english</language>
                                        <!-- 安装模式：installForAllUsers|installForCurrentUser|askTheUser -->
                                        <setupMode>installForAllUsers</setupMode>
                                        <!-- 创建桌面图标-->
                                        <createDesktopIconTask>true</createDesktopIconTask>
                                        <!-- 欢迎页 -->
                                        <disableWelcomePage>false</disableWelcomePage>
                                        <!-- 安装完成页 -->
                                        <disableFinishedPage>false</disableFinishedPage>
                                        <!-- 安装后运行 -->
                                        <disableRunAfterInstall>false</disableRunAfterInstall>
                                    </winConfig>
                                </configuration>
                            </execution>

                            <!-- 执行Mac打包 -->
                            <execution>
                                <id>bundling-for-mac</id>
                                <phase>package</phase>
                                <goals>
                                    <goal>package</goal>
                                </goals>
                                <configuration>
                                    <platform>mac</platform>
                                    <!-- 打压缩包：createZipball、createTarball-->
                                    <createZipball>true</createZipball>
                                    <macConfig>
                                        <!-- 图标 -->
                                        <icnsFile>${project.basedir}/dist/icon.icns</icnsFile>
                                        <!-- 打Dmg包，默认true-->
                                        <generateDmg>true</generateDmg>
                                        <!-- 不打Pkg包，默认true-->
                                        <generatePkg>false</generatePkg>
                                    </macConfig>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>

    <build>
        <finalName>${final.package.name}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>2.4</version>
                <configuration>
                    <appendAssemblyId>false</appendAssemblyId>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                    <archive>
                        <manifest>
                            <mainClass>${startup.class.name}</mainClass>
                        </manifest>
                    </archive>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>assembly</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- 脚本/命令执行插件 -->
            <!-- https://www.mojohaus.org/exec-maven-plugin/usage.html -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.1.0</version>
                <executions>
                    <execution>
                        <id>exe4j-pack</id>
                        <phase>package</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                        <configuration>
                            <executable>exe4jc</executable>
                            <arguments>
                                <argument>${project.basedir}\dist\pack.exe4j</argument>
                            </arguments>
                        </configuration>
                    </execution>

                    <execution>
                        <id>copy-app-jar</id>
                        <phase>package</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                        <configuration>
                            <executable>cp</executable>
                            <arguments>
                                <argument>${project.build.directory}\${final.package.name}.jar</argument>
                                <argument>${project.basedir}\dist\last-version</argument>
                            </arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.ftl</include>
                    <include>**/*.png</include>
                    <include>**/*.ico</include>
                    <include>**/*.html</include>
                    <include>**/*.properties</include>
                    <include>**/*.xls</include>
                    <include>**/*.txt</include>
                </includes>
            </resource>
        </resources>
    </build>

    <!--<distributionManagement>
        <repository>
            <id>ossrh</id>
            <url>https://oss.sonatype.org/service/local/staging/deploy/maven2</url>
        </repository>
        <snapshotRepository>
            <id>ossrh</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        </snapshotRepository>
    </distributionManagement>-->

</project>
</#noparse>