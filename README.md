# java-interface-test
 java自动化接口测试


1、此脚本测试的项目接口如下，需自行配置idea：
The project interface tested by this script is as follows, you need to configure the idea yourself:
https://github.com/ranmaxli/springboot-login

2、项目中涉及maven库中不存的lib，需要配置
The project involves lib not stored in the maven library, which needs to be configured
Project Structure  → Libraries  → Add 添加如下依赖：
json-lib-2.4-jdk15.jar
reportng-1.2.3-SNAPSHOT.jar
sqljdbc4-4.0.jar

3、项目中使用到了Lambdas表达式，需要配置
Lambdas expressions are used in the project and need to be configured
Project Structure  → Modules  → Language level:8-Lambdas, type annotations etc.

4、修改项目语言版本，JDK1.8
modify the project language version, JDK1.8
File  → Settings  → Build,Execution,Deployment → Compiler  → JavaCompiler  → 
Target bytecode version 1.8
