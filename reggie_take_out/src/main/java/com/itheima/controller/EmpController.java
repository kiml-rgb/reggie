package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import com.itheima.domain.Employee;
import com.itheima.service.EmpService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
