package com.itheima.service.impl;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.User;
import com.itheima.mapper.UserMapper;
import com.itheima.service.UserService;
import com.itheima.vo.R;
import org.springframework.stereotype.Service;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-17 16:03:41
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public R sendMsg(String phone) {
        if (!PhoneUtil.isMobile(phone)) return R.error("手机号码不合法");
        // 生成6位验证码
        String code = RandomUtil.randomNumbers(6);
        code = "1";
        System.out.println("code = " + code);
        return R.success(code);
    }
}
