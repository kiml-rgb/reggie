package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.anno.NoAuth;
import com.itheima.domain.User;
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
    @NoAuth
    public R sendMsg(@RequestBody Map<String, String> phoneMap) {
        String phone = phoneMap.get("phone");
        /*R result = userService.sendMsg(phone);
        // 将验证码存入session, 用于登录验证判断
        if (result.getCode() == 1) session.setAttribute(phone, result.getData());
        return result;*/
        return R.success(userService.sendMsg(phone));
    }

    @PostMapping("login")
    @NoAuth
    public R login(@RequestBody Map<String, String> phoneMap, HttpSession session) {
        R login = userService.login(phoneMap);
        if (login.getCode() == 1) session.setAttribute("user", login.getData());
        return login;
    }

    @PostMapping("loginout")
    public R logout(HttpSession session) {
        // 销毁session
        session.invalidate();
        return R.success(null);
    }
}