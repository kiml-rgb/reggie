package com.itheima.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Employee;
import com.itheima.mapper.EmpMapper;
import com.itheima.service.EmpService;
import com.itheima.vo.R;
import org.springframework.stereotype.Service;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description: Emp的业务实现类
 * @date 2022-09-11 16:44:13
 */
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Employee> implements EmpService {
    /**
     * 员工登陆业务
     * @param employee 员工
     * @return R
     */
    @Override
    public R login(Employee employee) {
        // 判断传入的参数是否合法
        if (ObjectUtil.isNull(employee)) return R.error("参数不合法");

        // 判断用户名或密码是否为空
        if (StrUtil.hasEmpty(employee.getUsername(), employee.getPassword())) return R.error("用户名或密码不能为空");

        // 根据用户名查询一个Emp对象
        Employee one = this.getOne(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getUsername, employee.getUsername()));

        // 判断用户名对应的用户是否存在
        if (ObjectUtil.isNull(one)) return R.error("用户名或密码错误");

        // 对输入的密码进行MD5加密
        String md5 = SecureUtil.md5(employee.getPassword());
        // 将加密后的密码与数据库中查询到的进行比较
        if (!StrUtil.equals(md5, one.getPassword(), true)) return R.error("用户名或密码错误");

        // 返回登陆成功数据
        return R.success(one);
    }
}
