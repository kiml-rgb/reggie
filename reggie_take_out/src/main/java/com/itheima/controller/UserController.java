package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
    public R sendMsg(@RequestBody Map<String, String> phoneMap, HttpSession session) {
        String phone = phoneMap.get("phone");
        R result = userService.sendMsg(phone);
        // 将验证码存入session, 用于登录验证判断
        if (result.getCode() == 1) session.setAttribute(phone, result.getData());
        return result;
    }

    @PostMapping("login")
    public R login(@RequestBody Map<String, String> phoneMap, HttpSession session) {
        String phone = phoneMap.get("phone");
        String code = phoneMap.get("code");

        String sessionCode = (String) session.getAttribute(phone);
        if (!StrUtil.equals(code, sessionCode)) {
            return R.error("验证码错误");
        }

        // 验证用户是否存在， 存在登录，不存在注册
        User user = userService.getOne(Wrappers.lambdaQuery(User.class).eq(User::getPhone, phone));
        if (ObjectUtil.isNull(user)) {
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }

        session.setAttribute("user", user);

        return R.success("登录成功");
    }

    @PostMapping("loginout")
    public R logout(HttpSession session) {
        // 销毁session
        session.invalidate();
        return R.success(null);
    }
}