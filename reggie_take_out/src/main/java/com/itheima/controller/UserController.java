package com.itheima.controller;

import cn.hutool.core.util.StrUtil;
import com.aliyuncs.utils.StringUtils;
import com.itheima.service.UserService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-17 15:52:15
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("sendMsg")
    public R sendMsg(@RequestBody Map<String, String> phoneMap, HttpSession session) {
        String phone = phoneMap.get("phone");
        R result = userService.sendMsg(phone);
        if (result.getCode() == 1) session.setAttribute(phone, result.getData());
        return result;
    }

    @PostMapping("login")
    public R login(@RequestBody Map<String, String> phoneMap, HttpSession session) {
        String phone = phoneMap.get("phone");
        String code = phoneMap.get("code");

        String sessionCode = (String) session.getAttribute(phone);
        if (StrUtil.equals(code, sessionCode)) return R.success("登录成功");
        return R.error("验证码错误");
    }

    @PostMapping("loginout")
    public R logout(HttpSession session) {
        // 销毁session
        session.invalidate();
        return R.success(null);
    }
}