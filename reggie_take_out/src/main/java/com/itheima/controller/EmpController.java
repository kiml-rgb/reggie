package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import com.itheima.anno.NoAuth;
import com.itheima.domain.Employee;
import com.itheima.dto.PageDto;
import com.itheima.service.EmpService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
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
    @NoAuth
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
        if ((empService.logout().getCode() == 1)) session.invalidate();
        return R.success("登出成功");
    }

    /**
     * 添加员工
     *
     * @param session  session
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
     *
     * @param empPageDto
     * @return R
     */
    @GetMapping("/page")
    public R findEmployeeByPage(PageDto empPageDto) {
        return empService.findEmployeeByPage(empPageDto);
    }

    /**
     * 更新 状态/员工
     * @param employee 员工
     * @param request request
     * @return
     */
    @PutMapping()
    public R updateStatusEmployee(@RequestBody Employee employee, HttpServletRequest request) {

        if (request.getHeader("Referer").contains("list.html")) {
            // 修改状态
            return empService.updateStatusEmployee(employee, "status");
        } else {
            // 修改信息
            return empService.updateStatusEmployee(employee, "info");
        }
    }

    /**
     * 根据id查询
     *
     * @param id id
     * @return R
     */
    @GetMapping("{id}")
    public R findEmployeeById(@PathVariable Long id) {
        return R.success(empService.getById(id));
    }
}
