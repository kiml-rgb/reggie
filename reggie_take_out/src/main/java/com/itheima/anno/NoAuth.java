package com.itheima.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-20 11:16:25
 */
/**
 * 自定义注解
 * 注解: 本质就是一个标记   自身有功能吗  没有任何功能
 * 注解写了之后一定要有人去解析这个注解
 *
 * 注解的生命周期
 * 默认的生命周期是   只存在于  源码阶段  (编译阶段/运行阶段)
 * 我们可以通过在这个注解上添加一些元注解来设置这个注解的生命周期
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoAuth {
    /* 注解中可以定义属性 */
    /* 属性名称后要加小括号  语法类似了接口中定义方法 */
    /* 属性后可以跟上default 来给这个属性赋默认值 */
    /* 如果这个属性有默认值 在使用这个注解的时候 这个属性可以不用赋值 */
    /* 如果这个属性没有默认值 在使用这个注解的时候就必须要给这个属性赋值 否则报错 */
    /* 如果属性的名称是value  并且只有这一个属性 在赋值的时候 属性名value可以省略不写 */
    /* 如果属性的名称是value 但是有多个属性 并且其他属性都有默认值 单独给这个value属性赋值的时候 属性名也可以省略不写 */
    /* 注解中支出的属性类型有: */
    /*  基本数据类型;
	    String;
	    枚举;
	    注解;
	    以上类型的数组;
	 */

    /*int value();

    String uername() default "";*/
}
