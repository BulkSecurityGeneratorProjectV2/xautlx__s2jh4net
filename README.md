## 项目简介

集结最新主流时尚开源技术的面向企业应用的Hybrid混合式APP及后端管理系统一体的的基础开发框架，
提供一套Java EE相关主流开源技术架构整合及企业级Web应用的设计实现的最佳实践和原型参考。

**http://www.entdiy.com**

### 项目托管同步更新GIT资源库：

**https://github.com/xautlx/s2jh4net**

**https://gitee.com/xautlx/s2jh4net**

[![Build Status](https://www.travis-ci.org/xautlx/s2jh4net.svg?branch=master)](https://www.travis-ci.org/xautlx/s2jh4net)

## 框架特色

* 面向主流企业级WEB应用系统的界面和常用基础功能设计实现
* 主体基于主流的（Java 8 + SpringMVC/Spring 5 + Hibernate 5.2/MyBatis 3/JPA 2.1/Spring Data 2）架构，详见[技术列表清单](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/100.技术列表.md)
* 基于Webpack/NodeJS/React/Cordova的Hybrid混合式APP与Restful API接口的集成应用
* 基于流行JQuery 1.12/Bootstrap 3.3等UI框架和插件扩展，良好的浏览器兼容性和移动设备访问支持
* 提供一个基础的代码生成框架，简化实现快速基本的CRUD功能开发
* 基于Maven的模块化项目和组件依赖管理模式，便捷高效的与持续集成开发集成

## 技术架构

* [技术列表](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/100.技术列表.md) - 框架主要技术(Java/Web/Tool)组件列表介绍
* [技术特性](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/110.技术特性.md) - 主要技术选型和设计说明
* [异常处理](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/120.异常处理.md) - 介绍框架的异常处理的策略设计
* [混合式APP](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/130.混合式APP.md) - 基于React+Cordova架构的Hybrid混合式APP
* [开发计划](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/140.开发计划.md) - 规划引入的新功能新特性
* [更新记录](CHANGELOG.md) - 了解项目架构设计和功能层面的主要版本更新记录

## 开发指南

* [演示运行](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/210.演示运行.md) - 一键运行脚本全自动完成基于Maven构建并Docker部署运行应用
* [Docker服务](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/220.Docker服务.md) - 介绍框架对Docker化服务部署在框架中的应用展示
* [开发配置](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/230.开发配置.md) - 开发基础环境配置说明
* [工程结构](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/240.工程结构.md) - 对整个项目工程代码结构进行概要性介绍
* [代码生成](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/250.代码生成.md) - 用于基本CURD框架代码生成的工具
* [基础功能](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/260.基础功能.md) - 框架已经实现的基础功能介绍说明
* [UI组件](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/270.UI组件.md)    - 框架UI组件设计思路和用法演示
* [表格组件](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/280.表格组件.md) - 功能强大的Grid表格组件扩展增强
* [表单控制](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/290.表单控制.md) - 介绍Web开发过程最主要的表单处理设计
* [常见问题](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/295.常见问题.md) - 对框架相关常见问题FAQ说明

## 核心模块

* [基础数据](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/310.基础数据.md) - 介绍框架对于基础数据及开发测试数据的设计思路
* [数据审计](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/320.数据审计.md) - 基于Hibernate Envers组件实现业务数据变更审计记录
* [计划任务](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/330.计划任务.md) - 基于Quartz组件实现计划任务的配置监控管理
* [API接口设计](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/340.API接口设计.md) - 介绍API接口部分设计思路

## 项目主站

**http://www.entdiy.com**

整个站点为Ngrok穿透访问家用台式机，可能存在访问缓慢情况或更新时短暂中断。同时为了防止随意数据变更导致系统崩溃，对个别功能启用了演示禁用控制。

如需完整体验建议参考 [演示运行](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/210.演示运行.md) 或 
[开发配置](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/230.开发配置.md) 在本地运行。

## 截图展示

* 后台系统All In One样例集中展示页面

![ui-example](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/images/ui-example.jpg)

* React/Cordova Hybrid APP界面展示

![ui-example](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/images/app-example.png)

### 许可说明

按照流行的社区版(Community Edition)和专业版(Professional Edition)运作模式。
详见 [许可说明](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/LICENSE.md)

* 社区版技术交流渠道

如果你觉得这个项目对你有所价值，请动动手指点击页面右上角的"Star"为项目加油助力；
当然如果你还想请作者喝杯咖啡，请点击页面上下方的"捐赠"，感谢你的支持！

QQ群讨论组：303438676 或提交到Git平台的Issue：

https://github.com/xautlx/s2jh4net/issues , https://gitee.com/xautlx/s2jh4net/issues

* 专业版咨询及技术支持

除了开发框架，如果还对诸如利用VMWare ESXi、虚拟Mac OSX、React/Cordova混合式APP、Jenkins持续集成Java Web/Android和iOS APP自动化构建分发、Docker化开发测试运维部署、Ngrok/HTTPS内网穿透服务、
JRebel(远程)热部署开发模式等DevOps理念和工具以全面提升开发测试运维等整个技术团队综合实力、研发效率和交付质量等主题感兴趣，详情可访问了解 [专业版资源摘要](entdiy-devops/entdiy-dev-guide/src/main/resources/META-INF/resources/dev/docs/markdown/LICENSE.md#professional_edition_introduction)

EMail: xautlx@hotmail.com 或 QQ: 2414521719 ，由于个人精力有限，专业版咨询方式仅限专业版咨询和付费用户，普通技术咨询请通过上述社区版渠道沟通，敬请理解。

### 参考引用

欢迎关注作者其他项目：

* [Nutch 2.X AJAX Plugins (Active)](https://github.com/xautlx/nutch-ajax) -  基于Apache Nutch 2.3和Htmlunit, Selenium WebDriver等组件扩展，实现对于AJAX加载类型页面的完整页面内容抓取，以及特定数据项的解析和索引

* [S2JH4Net (Active)](https://github.com/xautlx/s2jh4net) -  基于Spring MVC+Spring+JPA+Hibernate的面向互联网及企业Web应用开发框架

* [S2JH (Deprecated)](https://github.com/xautlx/s2jh) -  基于Struts2+Spring+JPA+Hibernate的面向企业Web应用开发框架
 
* [Nutch 1.X AJAX Plugins (Deprecated)](https://github.com/xautlx/nutch-htmlunit) -  基于Apache Nutch 1.X和Htmlunit的扩展实现AJAX页面爬虫抓取解析插件
 
* [12306 Hunter (Deprecated)](https://github.com/xautlx/12306-hunter) - （功能已失效不可用，不过还可以当作Swing开发样列参考之用）Java Swing C/S版本12306订票助手，用处你懂的

