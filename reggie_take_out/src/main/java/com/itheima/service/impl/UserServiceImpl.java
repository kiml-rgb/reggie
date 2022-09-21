package com.itheima.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.User;
import com.itheima.mapper.UserMapper;
import com.itheima.service.UserService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-17 16:03:41
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public R sendMsg(String phone) {
        if (!PhoneUtil.isMobile(phone)) return R.error("手机号码不合法");
        // 生成6位验证码
        String code = RandomUtil.randomNumbers(6);
        System.out.println("code = " + code);
        redisTemplate.opsForValue().set(phone + "_CODE", code);
        return R.success(code);
    }

    @Override
    public R login(Map<String, String> phoneMap) {
        String phone = phoneMap.get("phone");
        String code = phoneMap.get("code");

        String redisCode = (String) redisTemplate.opsForValue().get(phone + "_CODE");

        if (!StrUtil.equals(code, redisCode)) {
            return R.error("验证码错误");
        }

        // 验证用户是否存在， 存在登录，不存在注册
        User user = this.getOne(Wrappers.lambdaQuery(User.class).eq(User::getPhone, phone));
        if (ObjectUtil.isNull(user)) {
            user = new User();
            user.setPhone(phone);
            this.save(user);
        }

        return R.success(user);
    }
}
