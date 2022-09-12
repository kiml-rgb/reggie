package com.itheima.service.impl;

import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Employee;
import com.itheima.mapper.EmpMapper;
import com.itheima.service.EmpService;
import com.itheima.utils.EmpThreadLocal;
import com.itheima.vo.R;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

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
     *
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

        // 判断账户是否可用
        if (one.getStatus() == 0) return R.error("该账户已被禁用");

        // 返回登陆成功数据
        return R.success(one);
    }

    @Override
    public R addEmployee(Employee employee) {
        // 判断传入的参数是否合法
        if (ObjectUtil.isNull(employee)) return R.error("参数不合法");

        if (ObjectUtil.hasEmpty(employee.getName(), employee.getUsername(), employee.getPhone(),
                employee.getIdNumber())) return R.error("参数不合法");

        if (!PhoneUtil.isMobile(employee.getPhone())) return R.error("手机号码格式不正确");

        if (!IdcardUtil.isValidCard(employee.getIdNumber())) return R.error("身份证号码格式不正确");

        Employee one = this.getOne(Wrappers.lambdaQuery(Employee.class).eq(Employee::getUsername, employee.getUsername()));
        if (ObjectUtil.isNotNull(one)) return R.error("用户名已被占用");

        // 设置初始密码123456
        employee.setPassword(SecureUtil.md5("123456"));

        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        // employee.setCreateUser(EmpThreadLocal.get().getId());
        // employee.setUpdateUser(EmpThreadLocal.get().getId());

        if (this.save(employee)) {
            return R.builder().code(1).msg("添加成功").data("添加成功").build();
        }

        return R.error("添加失败，请稍后重试");
    }

    @Override
    public R findEmployeeByPage(Integer page, Integer pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        this.page(pageInfo,
                Wrappers.lambdaQuery(Employee.class)
                        .like(StrUtil.isNotEmpty(name), Employee::getName, name)
                        .orderByDesc(Employee::getUpdateTime));

        return R.success(pageInfo);
    }

    @Override
    public R updateStatusEmployee(Employee employee) {
        // 判断传入的参数是否合法
        if (ObjectUtil.isNull(employee)) return R.error("参数不合法");

        Employee one = this.getById(employee.getId());

        // 更新状态
        one.setStatus(one.getStatus() == 1 ? 0 : 1);

        // 更新更新时间
        one.setUpdateTime(LocalDateTime.now());

        // 更新更新人
        one.setUpdateUser(EmpThreadLocal.get().getId());

        if (this.updateById(one)) {
            return R.builder().code(1).msg("更新成功").data("更新成功").build();
        }

        return R.error("更新失败，请稍后重试");
    }


}
