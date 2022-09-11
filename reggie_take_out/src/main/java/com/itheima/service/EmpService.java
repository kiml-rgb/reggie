package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Employee;
import com.itheima.vo.R;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-11 16:31:13
 */
public interface EmpService extends IService<Employee> {

    /**
     * 员工登陆
     * @param employee 员工
     * @return R
     */
    R login(Employee employee);
}
