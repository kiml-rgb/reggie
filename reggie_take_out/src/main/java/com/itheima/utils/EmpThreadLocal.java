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

    // 传递一个Employee对象，⑧对象绑定到线程
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
