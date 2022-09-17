package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.User;
import com.itheima.vo.R;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-17 16:02:06
 */
public interface UserService extends IService<User> {
    /**
     * 发送验证码
     * @param phone phone
     * @return R
     */
    R sendMsg(String phone);
}
