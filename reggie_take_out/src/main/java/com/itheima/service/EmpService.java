package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Employee;
import com.itheima.dto.PageDto;
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

    /**
     * 添加员工
     * @param employee 员工
     * @return R
     */
    R addEmployee(Employee employee);

    /**
     * 分页查询
     * @param empPageDto empPageDto
     * @return R
     */
    R findEmployeeByPage(PageDto empPageDto);

    /**
     * 启用/禁用状态
     * @param employee employee
     * @param string 信息/状态
     * @return R
     */
    R updateStatusEmployee(Employee employee, String string);

    /**
     * 登出
     */
    R logout();
}
