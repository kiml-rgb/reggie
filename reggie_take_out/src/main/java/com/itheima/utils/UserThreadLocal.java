package com.itheima.utils;

import com.itheima.domain.Employee;
import com.itheima.domain.User;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 14:49:07
 */
public class UserThreadLocal {
    private static final ThreadLocal<String> tl = new ThreadLocal<>();

    // 传递一个Employee对象，⑧对象绑定到线程
    public static void set(String phone) {
        tl.set(phone);
    }

    // 从当前线程上获取绑定EMP的对象
    public static String get() {
        return tl.get();
    }

    // 从当前线程上解绑EMP的对象
    public static void remove() {
        tl.remove();
    }
}
