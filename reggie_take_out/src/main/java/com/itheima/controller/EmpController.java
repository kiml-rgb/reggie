package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import com.itheima.domain.Employee;
import com.itheima.service.EmpService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description: 员工相关接口
 * @date 2022-09-11 16:22:52
 */
@RestController
@RequestMapping("/employee")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 用户登录接口
     *
     * @param employee 用户
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody Employee employee, HttpSession session) {
        R loginR = empService.login(employee);
        if (ObjectUtil.isNotNull(loginR.getData())) session.setAttribute("employee", loginR.getData());
        return loginR;
    }

    /**
     * 用户退出登录接口
     *
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public R logout(HttpSession session) {
        // 销毁session
        session.invalidate();
        return R.success(null);
    }

    /**
     * 添加员工
     * @param session session
     * @param employee 员工
     * @return R
     */
    @PostMapping
    public R addEmployee(HttpSession session, @RequestBody Employee employee) {
        Employee emp = (Employee) session.getAttribute("employee");
        if (ObjectUtil.isNull(emp)) return R.error("请先登陆");
        return empService.addEmployee(employee);
    }

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页数量
     * @param name 条件查询 名字
     * @return R
     */
    @GetMapping("/page")
    public R findEmployeeByPage(Integer page, Integer pageSize, String name) {
        return empService.findEmployeeByPage(page, pageSize, name);
    }

    @PutMapping()
    public R updateStatusEmployee(@RequestBody Employee employee, HttpSession session) {
        Employee emp = (Employee) session.getAttribute("employee");
        if (ObjectUtil.isNull(emp)) return R.error("请先登陆");
        return empService.updateStatusEmployee(employee);
    }
}
