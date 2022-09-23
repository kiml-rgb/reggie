---
title: 瑞吉外卖项目总结
tags:
  - Java
categories:
  - Java
  - projects
  - reggie
abbrlink: 6195f8ba
date: 2022-09-19 15:53:33
typora-root-url: reggie-README
---

> 本项目使用springboot + mybatisplus进行开发。所有接口都已经给出，返回参数也都已经定义好，只有在部分模块涉及到多表联查，算是一个比较简单的入门项目。
>
> 项目地址如下：
>
> https://github.com/kiml-rgb/reggie 
>
> https://gitee.com/kiml18058625598/reggie

<!--more-->

------

## 瑞吉外卖项目介绍

### 项目介绍

![项目界面](image-20210726000655646.png)

本项目（瑞吉外卖）是专门为餐饮企业（餐厅、饭店）定制的一款软件产品，包括 系统管理后台 和 移动端应用 两部分。其中系统管理后台主要提供给餐饮企业内部员工使用，可以对餐厅的分类、菜品、套餐、订单、员工等进行管理维护。移动端应用主要提供给消费者使用，可以在线浏览菜品、添加购物车、下单等。



本项目共分为3期进行开发：

| 阶段   | 功能实现                                                     |
| ------ | ------------------------------------------------------------ |
| 第一期 | 主要实现基本需求，其中移动端应用通过H5实现，用户可以通过手机浏览器访问 |
| 第二期 | 主要针对移动端应用进行改进，使用微信小程序实现，用户使用起来更加方便 |
| 第三期 | 主要针对系统进行优化升级，提高系统的访问性能                 |



### 产品原型

**1. 管理端**

餐饮企业内部员工使用。 主要功能有: 

| 模块      | 描述                                                         |
| --------- | ------------------------------------------------------------ |
| 登录/退出 | 内部员工必须登录后,才可以访问系统管理后台                    |
| 员工管理  | 管理员可以在系统后台对员工信息进行管理，包含查询、新增、编辑、禁用等功能 |
| 分类管理  | 主要对当前餐厅经营的 菜品分类 或 套餐分类 进行管理维护， 包含查询、新增、修改、删除等功能 |
| 菜品管理  | 主要维护各个分类下的菜品信息，包含查询、新增、修改、删除、启售、停售等功能 |
| 套餐管理  | 主要维护当前餐厅中的套餐信息，包含查询、新增、修改、删除、启售、停售等功能 |
| 订单明细  | 主要维护用户在移动端下的订单信息，包含查询、取消、派送、完成，以及订单报表下载等功能 |

**2. 用户端**

移动端应用主要提供给消费者使用。主要功能有:

| 模块        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| 登录/退出   | 在移动端, 用户也需要登录后使用APP进行点餐                    |
| 点餐-菜单   | 在点餐界面需要展示出菜品分类/套餐分类, 并根据当前选择的分类加载其中的菜品信息, 供用户查询选择 |
| 点餐-购物车 | 用户选中的菜品就会加入用户的购物车, 主要包含 查询购物车、加入购物车、删除购物车、清空购物车等功能 |
| 订单支付    | 用户选完菜品/套餐后, 可以对购物车菜品进行结算支付, 这时就需要进行订单的支付 |
| 个人信息    | 在个人中心页面中会展示当前用户的基本信息, 用户可以管理收货地址, 也可以查询历史订单数据 |



### 技术选型

![技术选型](image-20210726005437857.png)

**1. 用户层**

本项目中在构建系统管理后台的前端页面，我们会用到H5、Vue.js、ElementUI等技术。而在构建移动端应用时，我们会使用到微信小程序。



**2. 网关层**

Nginx是一个服务器，主要用来作为Http服务器，部署静态资源，访问性能高。在Nginx中还有两个比较重要的作用： 反向代理和负载均衡， 在进行项目部署时，要实现Tomcat的负载均衡，就可以通过Nginx来实现。



**3. 应用层**

SpringBoot： 快速构建Spring项目, 采用 "约定优于配置" 的思想, 简化Spring项目的配置开发。

Spring: 统一管理项目中的各种资源(bean), 在web开发的各层中都会用到。

SpringMVC：SpringMVC是spring框架的一个模块，springmvc和spring无需通过中间整合层进行整合，可以无缝集成。

SpringSession: 主要解决在集群环境下的Session共享问题。

lombok：能以简单的注解形式来简化java代码，提高开发人员的开发效率。例如开发中经常需要写的javabean，都需要花时间去添加相应的getter/setter，也许还要去写构造器、equals等方法。

Swagger： 可以自动的帮助开发人员生成接口文档，并对接口进行测试。



**4. 数据层**

MySQL： 关系型数据库, 本项目的核心业务数据都会采用MySQL进行存储。

MybatisPlus： 本项目持久层将会使用MybatisPlus来简化开发, 基本的单表增删改查直接调用框架提供的方法即可。

Redis： 基于key-value格式存储的内存数据库, 访问速度快, 经常使用它做缓存(降低数据库访问压力, 提供访问效率), 在后面的性能优化中会使用。



**5. 工具**

git: 版本控制工具, 在团队协作中, 使用该工具对项目中的代码进行管理。

maven: 项目构建工具。

junit：单元测试工具，开发人员功能实现完毕后，需要通过junit对功能进行单元测试。



### 功能架构

![功能架构](image-20210726122825225.png)

**1. 移动端前台功能**

手机号登录 , 微信登录 , 收件人地址管理 , 用户历史订单查询 , 菜品规格查询 , 购物车功能 , 下单 , 分类及菜品浏览。

**2. 系统管理后台功能**

员工登录/退出 , 员工信息管理 , 分类管理 , 菜品管理 , 套餐管理 , 菜品口味管理 , 订单管理 。



### 角色

在瑞吉外卖这个项目中，存在以下三种用户，这三种用户对应三个角色：后台系统管理员、后台系统普通员工、C端(移动端)用户。

| 角色             | 权限操作                                                     |
| ---------------- | ------------------------------------------------------------ |
| 后台系统管理员   | 登录后台管理系统，拥有后台系统中的所有操作权限               |
| 后台系统普通员工 | 登录后台管理系统，对菜品、套餐、订单等进行管理 (不包含员工管理) |
| C端用户          | 登录移动端应用，可以浏览菜品、添加购物车、设置地址、在线下单等 |

------

## 开发环境搭建

### 数据库环境搭建

#### 创建数据库

> 注意: 本项目数据库的字符串, 选择 utf8mb4

本项目数据库sql语句在项目文件下

![reggie.sql](image-20220919172356989.png)



#### 数据表介绍

本项目涉及到的数据表如下

| **序号** | **表名**      | **说明**         |
| -------- | ------------- | ---------------- |
| 1        | employee      | 员工表           |
| 2        | category      | 菜品和套餐分类表 |
| 3        | dish          | 菜品表           |
| 4        | setmeal       | 套餐表           |
| 5        | setmeal_dish  | 套餐菜品关系表   |
| 6        | dish_flavor   | 菜品口味关系表   |
| 7        | user          | 用户表（C端）    |
| 8        | address_book  | 地址簿表         |
| 9        | shopping_cart | 购物车表         |
| 10       | orders        | 订单表           |
| 11       | order_detail  | 订单明细表       |



### Maven项目搭建

#### 创建maven项目

1. 在idea中创建maven project
2. 检查项目编码（设置均为UTF-8）
3. 检查maven配置
4. 检查JDK版本（JDK的版本选择1.8）



#### 搭建基础环境

1. 在`pom.xml`中导入依赖
2. 在工程的resources目录下创建`application.yml`文件,并引入配置
3. 编写启动类`App.class`

```java
@SpringBootApplication
@MapperScan("com.itheima.mapper")
@EnableTransactionManagement
@Slf4j
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        log.info("项目启动成功...");
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }
}
```

> @Slf4j : 
>
> 是lombok中提供的注解, 用来通过slf4j记录日志。

当搭建完上述的基础环境之后, 就可以通过引导类, 启动该项目。



#### 前端静态资源导入

本项目的前端代码已经提供，不需要编写，只要能看懂即可，后续需要查看前端需要的数据，定义一些数据。

![前端静态资源](image-20220919174107850.png)

直接将静态资源bacakend和front文件放到static目录文件下

Spring Boot 默认就配置了`/static/**`映射，所以无需任何配置就能访问此目录下的静态资源

> 在Springboot项目中, 默认静态资源的存放目录为 : "classpath:/resources/", "classpath:/static/", "classpath:/public/"

如果不将文件存放在springboot默认配置的加载目录，那么这个时候想要访问到静态资源，就要设置静态资源映射。

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
}
```



### 基础类导入

#### 实体类导入

创建实体类包，将所有实体类直接导入

![实体类](image-20220920090521628.png)



#### vo和dto导入

![vo和dto](/image-20220920101609748.png)

> VO（View Object）：视图对象，用于展示层，它的作用是把某个指定页面（或组件）的所有数据封装起来。通常起的类名会加上VO。
>
> DTO（Data Transfer Object）：泛指用于展示层与服务层之间的数据传输对象。用于Controller中接收的参数较多时，定义一个对象专门用于接收这些对象。

------

## 业务逻辑

### 后台系统登录

1. 判断传入的参数是否合法
2. 判断用户名或密码是否为空
3. 根据用户名查询一个Emp对象
4. 判断用户名对应的用户是否存在
5. 对输入的密码进行MD5加密
6. 将加密后的密码与数据库中查询到的进行比较
7. 返回登陆成功数据

```java
/**
 * 员工登陆业务
 * @param employee 员工
 * @return R
 */
@Override
public R login(Employee employee) {
    // 判断传入的参数是否合法
    if (ObjectUtil.isNull(employee)) return R.error("参数不合法");

    // 判断用户名或密码是否为空
    if (StrUtil.hasEmpty(employee.getUsername(), employee.getPassword())) return R.error("用户名或密码不能为空");

    // 根据用户名查询一个Emp对象
    Employee one = this.getOne(Wrappers.lambdaQuery(Employee.class)
            .eq(Employee::getUsername, employee.getUsername()));

    // 判断用户名对应的用户是否存在
    if (ObjectUtil.isNull(one)) return R.error("用户名或密码错误");

    // 对输入的密码进行MD5加密
    String md5 = SecureUtil.md5(employee.getPassword());
    // 将加密后的密码与数据库中查询到的进行比较
    if (!StrUtil.equals(md5, one.getPassword(), true)) return R.error("用户名或密码错误");

    // 返回登陆成功数据
    return R.success(one);
}
```



### 新增员工功能

1. 判断传入的参数是否合法
2. 判断手机号码格式
3. 判断身份证号码格式
4. 判断用户名是否被占用
5. 设置初始密码123456，使用MD5加密
6. 设置更新时间和新建时间，此处使用Mybatis Plus的自动填充

```java
@Override
public R addEmployee(Employee employee) {
    // 判断传入的参数是否合法
    if (ObjectUtil.isNull(employee)) return R.error("参数不合法");

    if (ObjectUtil.hasEmpty(employee.getName(), employee.getUsername(), employee.getPhone(),
            employee.getIdNumber())) return R.error("参数不合法");

    // 判断手机号码格式
    if (!PhoneUtil.isMobile(employee.getPhone())) return R.error("手机号码格式不正确");

    // 判断身份证号码格式
    if (!IdcardUtil.isValidCard(employee.getIdNumber())) return R.error("身份证号码格式不正确");

    // 判断用户名是否被占用
    Employee one = this.getOne(Wrappers.lambdaQuery(Employee.class).eq(Employee::getUsername, employee.getUsername()));
    if (ObjectUtil.isNotNull(one)) return R.error("用户名已被占用");

    // 设置初始密码123456 使用MD5加密
    employee.setPassword(SecureUtil.md5("123456"));

    // 自动填充代替
    // employee.setCreateTime(LocalDateTime.now());
    // employee.setUpdateTime(LocalDateTime.now());
    // employee.setCreateUser(EmpThreadLocal.get().getId());
    // employee.setUpdateUser(EmpThreadLocal.get().getId());

    if (this.save(employee)) {
        return R.builder().code(1).msg("添加成功").data("添加成功").build();
    }

    return R.error("添加失败，请稍后重试");
}
```

#### 格式判断-工具包的使用

此处参数校验的时候，需要多处校验手机号，身份证等是否合法。当然可以自定义正则校验完成，但是提供相应功能的工具包有很多（能偷懒谁自己写啊🥱），此处使用的是hutool工具包，只需要在pom.xml中添加依赖就行。



### 修改时判断用户名重复

1. 通过id找到数据库中的对象
2. 判断数据库中的对象的名字与当前传入对象的名字是否相同
3. 如果不同，说明修改了用户名，则需要判断修改后的用户名在数据库中是否有重名对象

```java
Employee oldEmp = this.getById(employee);
if (!StrUtil.equals(oldEmp.getUsername(), employee.getUsername())) {
    Employee one = this.getOne(Wrappers.lambdaQuery(Employee.class).eq(Employee::getUsername, employee.getUsername()));
    // 如果业务允许修改用户名，得先判断用户输入的用户名是否和之前一样
    if (ObjectUtil.isNotNull(one)) return R.error("用户名已被占用");
}
```



### 文件的上传和下载

在添加菜品时，涉及到图片的上传操作，以下是一般流程

文件上传前的必须操作

1. 校验

   校验文件是否为空

   校验文件的格式

   校验文件的大小

2. 给文件进行重命名

3. 给文件目录分离

   使用目录分离算法(Hash 把hash值作为目录名称)

   按照日期分目录(每天创建一个新的目录)

   按照用户名分目录

4. 图片保存地点

   web服务器（项目所运行在的电脑） 

   云存储服务商

   自己搭建文件服务器（FastDIS Minio）

```java
package com.itheima.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.itheima.vo.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-16 11:38:44
 */
@RestController
@RequestMapping("common")
public class CommonController {

    @PostMapping("upload")
    public R uploadPic(MultipartFile file) throws IOException {
        // 校验
        // 校验文件是否为空
        if (ObjectUtil.isNull(file)) return R.error("上传文件不能为空");
        // 校验文件的格式
        if (!StrUtil.containsAnyIgnoreCase(FileUtil.getSuffix(file.getOriginalFilename()), "jpg", "png")) {
            return R.error("上传文件格式错误");
        }
        // 校验文件的大小
        if (file.getSize() / 1024 / 1024 > 2) return R.error("文件过大，限制在2MB以内");
        // 给文件进行重命名
        String fileName = UUID.fastUUID().toString() + "." + FileUtil.getSuffix(file.getOriginalFilename());
        // 给文件目录分离
        // 图片保存地点 web服务器（项目所运行在的电脑） 云存储服务商 自己搭建文件服务器（FastDIS Minio）

        /* 如果我们把图片保存到当前电脑的其他盘符的其他目录下(非项目的static目录) */
        /* 那么就需要使用编码的方式去读取这个文件流 并且通过Response响应这个流 */
        /* 但是如果我们把图片保存到当前这个项目的static目录中 就可以直接返回一个访问这个图片的地址字符串 */
        file.transferTo(new File("D:\\Develop\\workspace\\reggie\\reggie_take_out\\src\\main\\resources\\static\\images\\" + fileName));
        return R.success(fileName);
    }

    @GetMapping("download")
    public void downloadPic(String name, HttpServletResponse response) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\Develop\\workspace\\reggie\\reggie_take_out\\src\\main\\resources\\static\\images\\" + name);
        ServletOutputStream outputStream = response.getOutputStream();

        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }

        fileInputStream.close();
        outputStream.close();
    }
}
```

**文件上传相关：**

![文件上传的实现方式](/image-20220920210039909.png)

![文件上传的三要素](/image-20220920210125183.png)

![文件上传的原理](/image-20220920210202727.png)



### 发送短信

前台用户采用验证码登陆的方式，这里验证码发送采用连接阿里云实现。

1. 集成阿里云平台

   申请签名

   申请模板 （即短信的内容） （一个签名可以对应多个模板）

   配置AK和SK （推荐使用子账户AK，配置权限）

1. pom.xml文件中添加依赖

```xml
<!--阿里云短信发送-->
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-dysmsapi</artifactId>
    <version>1.1.0</version>
</dependency>
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-core</artifactId>
    <version>4.6.0</version>
</dependency>
```

2. 用户登陆Service层代码如下：

```java
@Override
public R sendMsg(String phone) {
    if (!PhoneUtil.isMobile(phone)) return R.error("手机号码不合法");
    
    // 生成6位验证码
    String code = RandomUtil.randomNumbers(6);
    // 测试通过后不再发送验证码，采用随机生成数据，控制台输出的方式进行后续测试。
    SMSUtils.sendSM(phone,code);
    /*System.out.println("code = " + code);*/
    
    redisTemplate.opsForValue().set(phone + "_CODE", code);
    return R.success(code);
}
```

3. 发送验证码的SMSUtils代码如下

```java
package com.itheima.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Properties;

/**
 * 阿里云短信发送工具类
 *
 * @author yunfei
 * @date 2022年09月17日 14:54
 */
public class SMSUtils {

    /* DI的前提是当前的这个Bean要在容器中 */
    /*@Value("aliyun.sms.ak")
    private String ak;*/

    private static String ak;
    private static String sk;
    private static String signName;
    private static String templateCode;

    /* Java中通常都会使用类加载去读取文件 */
    static {
        try {
            Properties properties = new Properties();
            properties.load(SMSUtils.class.getClassLoader()
                            .getResourceAsStream("aliyun.properties"));
            ak = properties.getProperty("aliyun.sms.ak");
            sk = properties.getProperty("aliyun.sms.sk");
            signName = properties.getProperty("aliyun.sms.signName");
            templateCode = properties.getProperty("aliyun.sms.templateCode");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SendSmsResponse sendSM(String phoneNum, String code) {
        // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ak, sk);
        IAcsClient client = new DefaultAcsClient(profile);

        // 创建一个 AssumeRoleRequest 并设置请求参数
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNum);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse response = null;
        try {
            // 发起请求，并得到response
            response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
        return response;
    }
}
```

4. 由于此工具类不想只依赖于springboot环境，因此采用读取properties文件的形式加载连接阿里云必要的信息。在工具类中采用静态代码块，读取配置类文件中的信息。

```properties
aliyun.sms.ak=LTAI5tHnZ8r3424CCbqFD1xZ
aliyun.sms.sk=bo0835BR3Pn8upjtQ0AcHz9vT4mV9Q
aliyun.sms.signName=\u676D\u5DDE\u9ED1\u9A6C\u4F20\u667A\u5065\u5EB7
aliyun.sms.templateCode=SMS_184110818
```



### 提交订单

本操作涉及的表较多，是本项目中业务逻辑最复杂的一个模块，但也很简单。

1. 设置订单默认参数
2. 获取购物车信息，==计算订单总金额==
3. 获取地址簿收货人信息， 设置订单中收货人的信息
4. 将购物车信息==拷贝==，插入订单明细
5. 存储信息
6. ❗清空购物车

```java
    @Override
    @Transactional
    public R submitOrder(Orders orders) {
        if (ObjectUtil.isNull(orders.getAddressBookId())) return R.error("参数不合法");

        // 使用雪花算法生成一个Id
        orders.setNumber(StrUtil.toString(IdWorker.getId()));
        // 设置订单状态 默认是4
        orders.setStatus(4);
        orders.setUserId(UserThreadLocal.get().getId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());

        List<ShoppingCart> list = shoppingCartService.list(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, UserThreadLocal.get().getId()));

        // 订单总金额
        BigDecimal sum = BigDecimal.ZERO;
        for (ShoppingCart shoppingCart : list) {
            BigDecimal amount = shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber()));
            sum = sum.add(amount);
        }

        // 设置金额
        orders.setAmount(sum);

        // 查询收货人信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());

        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setUserName(UserThreadLocal.get().getName());
        orders.setConsignee(addressBook.getConsignee());

        this.save(orders);

        // 订单明细
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : list) {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, detail, "id");
            detail.setOrderId(orders.getId());
            orderDetails.add(detail);
        }

        orderDetailService.saveBatch(orderDetails);

        // 清空购物车
        shoppingCartService.remove(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, UserThreadLocal.get().getId()));
        return R.success(orders);
    }
```







## 代码增强

### 自定义拦截器

1. 创建MVC配置类

```java
package com.itheima.config;

import com.itheima.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 11:16:02
 */
@Configuration
public class MVCConfig implements WebMvcConfigurer{
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器， 对登录进行拦截，指定要拦截的请求（/**）
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**");
    }
}
```

2. 创建登录拦截器

```java
package com.itheima.interceptor;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.itheima.domain.Employee;
import com.itheima.domain.User;
import com.itheima.utils.EmpThreadLocal;
import com.itheima.utils.UserThreadLocal;
import com.itheima.vo.R;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 10:45:58
 */
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断本次请求是否需要拦截
        if (!(handler instanceof HandlerMethod)) return true;

        if (request.getRequestURI().contains("employee/login")) return true;
        if (request.getRequestURI().contains("user/login")) return true;
        if (request.getRequestURI().contains("user/sendMsg")) return true;

        if (ObjectUtil.isNotNull(request.getSession().getAttribute("employee"))) {
            EmpThreadLocal.set((Employee) request.getSession().getAttribute("employee"));
            return true;
        }

        if (ObjectUtil.isNotNull(request.getSession().getAttribute("user"))) {
            UserThreadLocal.set((User) request.getSession().getAttribute("user"));
            return true;
        }

        // 设置状态码401
        response.setStatus(401);

        // 设置编码格式
        response.setContentType("application/json;charset=utf-8");

        response.getWriter().write(JSONUtil.toJsonStr(R.error("用户未登录")));

        // 判断是否登录，没有直接返回401
        return false;
    }

    /**
     * 请求回到浏览器之前执行
     * @param request request
     * @param response response
     * @param handler handler
     * @param modelAndView modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserThreadLocal.remove();
        EmpThreadLocal.remove();
    }
}
```



自定义拦截器实现`HandlerInterceptor`，在此接口中有3个可以被重写的方法：

`preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)`

> 拦截处理程序的执行。在 HandlerMapping 确定适当的处理程序对象之后，但在 HandlerAdapter 调用处理程序之前调用。 DispatcherServlet 处理执行链中的处理程序，该处理程序由任意数量的拦截器组成，处理程序本身位于最后。使用此方法，每个拦截器都可以决定中止执行链，通常是发送 HTTP 错误或编写自定义响应。注意：特殊注意事项适用于异步请求处理。

调用时间：Controller方法处理之前

执行顺序：链式Intercepter情况下，Intercepter按照声明的顺序一个接一个执行

若返回false，则中断执行，**注意：不会进入afterCompletion**



`postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView)`

> 拦截处理程序的执行。在 HandlerAdapter 实际调用处理程序之后，但在 DispatcherServlet 呈现视图之前调用。可以通过给定的 ModelAndView 向视图公开其他模型对象。 DispatcherServlet 处理执行链中的处理程序，该处理程序由任意数量的拦截器组成，处理程序本身位于最后。使用此方法，每个拦截器都可以对执行进行后处理，以执行链的相反顺序应用。

调用前提：preHandle返回true

调用时间：Controller方法处理完之后，DispatcherServlet进行视图的渲染之前，也就是说在这个方法中你可以对ModelAndView进行操作

执行顺序：链式Intercepter情况下，Intercepter**按照声明的顺序倒着执行**。

备注：postHandle虽然post打头，但post、get方法都能处理



`afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex)`

> 请求处理完成后的回调，即渲染视图后。将在处理程序执行的任何结果上调用，从而允许适当的资源清理。注意：仅当此拦截器的 preHandle 方法已成功完成并返回 true 时才会调用！与 postHandle 方法一样，该方法将在链中的每个拦截器上以相反的顺序调用，因此第一个拦截器将是最后一个被调用的拦截器。

调用前提：preHandle返回true

调用时间：DispatcherServlet进行视图的渲染之后

多用于清理资源



### ThreadLocal

> 在Java的多线程编程中，为保证多个线程对共享变量的安全访问，通常会使用synchronized来保证同一时刻只有一个线程对共享变量进行操作。这种情况下可以将[类变量](https://baike.baidu.com/item/%E7%B1%BB%E5%8F%98%E9%87%8F)放到ThreadLocal类型的对象中，使变量在每个线程中都有独立拷贝，不会出现一个线程读取变量时而被另一个线程修改的现象。最常见的ThreadLocal使用场景为用来解决数据库连接、Session管理等。在本项目中，被用于Session管理。

在拦截器的代码中，使用了两个ThreadLocal，在登录成功后，将Session中的数据存到对应的ThreadLocal中，方便后续使用。自定义ThreadLocal的代码如下：

```java
package com.itheima.utils;

import com.itheima.domain.Employee;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 14:49:07
 */
public class EmpThreadLocal {
    private static final ThreadLocal<Employee> tl = new ThreadLocal<>();

    // 传递一个Employee对象，把对象绑定到线程
    public static void set(Employee employee) {
        tl.set(employee);
    }

    // 从当前线程上获取绑定EMP的对象
    public static Employee get() {
        return tl.get();
    }

    // 从当前线程上解绑EMP的对象
    public static void remove() {
        tl.remove();
    }
}
```

Servlet的运行是多线程的，但是应用服务器（这里指的是Tomcat）只会创建一个Servlet实例，这个实例被多个线程并发调用。每次调用doGet()方法，session可能被重置，导致存储混乱。ThreadLocal就是用于解决此问题。



### MybtisPlus自动填充

MybtisPlus提供自动填充的功能

1. 在需要填充的字段上方添加注解`@TableField(fill = FieldFill.INSERT)`

   需要在什么时候自动填充，FieldFill就选择什么

```java
/**
 * 创建时间
 */
@TableField(fill = FieldFill.INSERT)
private LocalDateTime createTime;

...
更新时间、创建人、修改人    
...
```

2. 实现MetaObjectHandler接口，重写对应的方法

```java
package com.itheima.config;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itheima.utils.EmpThreadLocal;
import com.itheima.utils.UserThreadLocal;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 15:14:37
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    // 当我们调用MP的新增方法时，会自动执行
    @Override
    public void insertFill(MetaObject metaObject) {
        // metaObject 我们调用新增方法时传递的对象
        if (metaObject.hasGetter("createTime") && ObjectUtil.isNull(getFieldValByName("createTime", metaObject)))
            setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        if (metaObject.hasGetter("updateTime") && ObjectUtil.isNull(getFieldValByName("updateTime", metaObject)))
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        if (metaObject.hasGetter("createUser") && ObjectUtil.isNull(getFieldValByName("createUser", metaObject)))
            setFieldValByName("createUser", ObjectUtil.isNotNull(EmpThreadLocal.get()) ? EmpThreadLocal.get().getId() : UserThreadLocal.get().getId(), metaObject);
        if (metaObject.hasGetter("updateUser") && ObjectUtil.isNull(getFieldValByName("updateUser", metaObject)))
            setFieldValByName("updateUser", ObjectUtil.isNotNull(EmpThreadLocal.get()) ? EmpThreadLocal.get().getId() : UserThreadLocal.get().getId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter("updateTime") && ObjectUtil.isNull(getFieldValByName("updateTime", metaObject)))
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        if (metaObject.hasGetter("updateUser") && ObjectUtil.isNull(getFieldValByName("updateUser", metaObject)))
            setFieldValByName("updateUser", ObjectUtil.isNotNull(EmpThreadLocal.get()) ? EmpThreadLocal.get().getId() : UserThreadLocal.get().getId(), metaObject);
    }
}
```



### MybatisPlus分页查询

> MybatisPlus提供的插件都将基于接口InnerInterceptor来实现功能，目前已有的功能包括分页查询。

1. 新建配置类，加入扫描。或直接在SpringBoot的启动类中配置如下代码：

```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
    mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return mybatisPlusInterceptor;
}
```

2. 使用方法如下

```java
@Override
public R findEmployeeByPage(EmpPageDto empPageDto) {
    empPageDto.check();

    // 分页对象
    Page<Employee> pageInfo = new Page<>(empPageDto.getPage(), empPageDto.getPageSize());

    this.page(pageInfo,
            Wrappers.lambdaQuery(Employee.class)
              		// 添加分页筛选相关条件
                    .like(StrUtil.isNotEmpty(empPageDto.getName()), Employee::getName, empPageDto.getName())
              		// 添加分页排序条件
                    .orderByDesc(Employee::getUpdateTime));

    // 可以直接取出分页查询得到的结果集，然后对结果集进行操作，修改结果集中单个元素的属性
    
    return R.success(pageInfo);
}
```



### Long类型精度丢失

1. 在员工信息修改时，需要通过查找，实现信息回显

2. 修改过后，发现回显功能不能实现，查看数据库，原有的id被改变，最后四位变成了0。

   服务端会将返回的R对象进行json序列化，转换为json格式的数据，而员工的ID是一个Long类型的数据，而且是一个长度为 19 位的长整型数据， 该数据返回给前端是没有问题的。

   但是在前端JS中， js在对长度较长的长整型数据进行处理时， 会损失精度， 从而导致提交的id和数据库中的id不一致。

3. 修改实体类，在Long类型的属性上添加一行注释，让系统序列化时，保留相关精度。

```java
@JsonSerialize(using = ToStringSerializer.class)
```



### 对象映射器

上述修改需要在每处用到的地方添加注释，在资料中看见另一种方法

1. 提供对象转换器JacksonObjectMapper，基于Jackson进行Java对象到json数据的转换

```java
package com.itheima.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 对象映射器:基于jackson将Java对象转为json，或者将json转为Java对象
 * 将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]
 * 从Java对象生成JSON的过程称为 [序列化Java对象到JSON]
 */
public class JacksonObjectMapper extends ObjectMapper {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public JacksonObjectMapper() {
        super();
        //收到未知属性时不报异常
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        //反序列化时，属性不存在的兼容处理
        this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        SimpleModule simpleModule = new SimpleModule()
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))

                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        //注册功能模块 例如，可以添加自定义序列化器和反序列化器
        this.registerModule(simpleModule);
    }
}
```

2. 在WebMvcConfig配置类中扩展Spring mvc的消息转换器，在此消息转换器中使用提供的对象转换器进行Java对象到json数据的转换

```java
@Override
public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    log.info("消息转换器...");
    // 创建消息转换器对象
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    // 设置对象转换器，底层使用Jackson将Java对象转为json
    converter.setObjectMapper(new JacksonObjectMapper());
    // 将上面的消息转换器对象追加到mvc框架的转换器集合中
    converters.add(0, converter);
}
```



### MybatisPlus一对多关系查询实现

当前端查询时，有时会需要提供多表数据，即返回的数据无法对应原有的实体类的情况，常用的方法是为前端请求的数据封装一个dto对象，在此项目中，采用的方法是在实体类中加入一个属性。

例如：在Dish实体类中加入

```java
// 数据库查询时，避免查询这个字段，造成sql语句错误
@TableField(exist = false)
private List<DishFlavor> flavors;
```

后续查询时可以对这个list直接set/get，实现业务需求。

❗❗❗需要注意的是，由于是将此实体类当成dto使用，定义的数据需要与前端请求的数据相同，即flavors不能改为flavorList。



### 事务的开启

涉及到多表数据，就会涉及到多表同时增删改，需要事务的加入，SpringBoot的事务开启需要在启动类上添加注解`@EnableTransactionManagement`，对于需要加入事务的方法则使用注解`@Transactional`



### MybatisPlus逻辑删除

MybatisPlus逻辑删除的实现只需要在实体类中加入如下：

```java
// value：默认逻辑未删除值 delval：默认逻辑删除值
@TableLogic(value = "0", delval = "1")
private Integer isDeleted;
```

❗❗❗逻辑删除后，此条数据在使用查询时，将被自动拼接删除条件，会造成原有代码在数据判重时的错误，如果依旧需要此业务，要重新写sql语句。在此项目中，没有使用逻辑删除。



