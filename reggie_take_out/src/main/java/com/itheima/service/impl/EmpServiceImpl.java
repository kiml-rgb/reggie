package com.itheima.service.impl;

import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Employee;
import com.itheima.dto.PageDto;
import com.itheima.mapper.EmpMapper;
import com.itheima.service.EmpService;
import com.itheima.utils.EmpThreadLocal;
import com.itheima.vo.R;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description: Emp的业务实现类
 * @date 2022-09-11 16:44:13
 */
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Employee> implements EmpService {
    @Autowired
    private RedisTemplate redisTemplate;

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

        // 登陆成功,存入redis
        // ToDo：照理说,这里应该加入token
        redisTemplate.opsForValue().set(
                "reggie_emp_login_" + one.getUsername(), JSONUtil.toJsonStr(one),
                60 * 2, TimeUnit.MINUTES);

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
    public R findEmployeeByPage(PageDto empPageDto) {
        empPageDto.check();

        Page<Employee> pageInfo = new Page<>(empPageDto.getPage(), empPageDto.getPageSize());

        this.page(pageInfo,
                Wrappers.lambdaQuery(Employee.class)
                        .like(StrUtil.isNotEmpty(empPageDto.getName()), Employee::getName, empPageDto.getName())
                        .orderByDesc(Employee::getUpdateTime));

        return R.success(pageInfo);
    }

    @Override
    public R updateStatusEmployee(Employee employee, String string) {
        if (StrUtil.equals(string, "info", true)) {
            // 判断传入的参数是否合法
            if (ObjectUtil.isNull(employee)) return R.error("参数不合法");
            if (ObjectUtil.hasEmpty(employee.getName(), employee.getUsername(), employee.getPhone(),
                    employee.getIdNumber())) return R.error("参数不合法");
            if (!PhoneUtil.isMobile(employee.getPhone())) return R.error("手机号码格式不正确");
            if (!IdcardUtil.isValidCard(employee.getIdNumber())) return R.error("身份证号码格式不正确");

            Employee oldEmp = this.getById(employee);
            if (!StrUtil.equals(oldEmp.getUsername(), employee.getUsername())) {
                Employee one = this.getOne(Wrappers.lambdaQuery(Employee.class).eq(Employee::getUsername, employee.getUsername()));
                // 如果业务允许修改用户名，得先判断用户输入的用户名是否和之前一样
                if (ObjectUtil.isNotNull(one)) return R.error("用户名已被占用");
            }

        } else {
            // 判断传入的参数是否合法
            if (ObjectUtil.isNull(employee) || ObjectUtil.hasEmpty(employee.getStatus(), employee.getId()))
                return R.error("参数不合法");
        }

        if (this.updateById(employee)) {
            return R.builder().code(1).msg("更新成功").data("更新成功").build();
        }

        return R.error("更新失败，请稍后重试");
    }

    @Override
    public R logout() {
        // 退出登陆,清空session
        redisTemplate.delete(
                "reggie_emp_login_" + EmpThreadLocal.get().getUsername());

        return R.success("登出成功");
    }


}
