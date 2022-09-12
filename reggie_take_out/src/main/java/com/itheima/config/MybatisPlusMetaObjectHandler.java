package com.itheima.config;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itheima.utils.EmpThreadLocal;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 15:14:37
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    // 当我们调用MP的新增方法时，会自动执行
    @Override
    public void insertFill(MetaObject metaObject) {
        // metaObject 我们调用新增方法时传递的对象
        if (ObjectUtil.isNull(getFieldValByName("createTime", metaObject)))
            setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        if (ObjectUtil.isNull(getFieldValByName("updateTime", metaObject)))
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        if (ObjectUtil.isNull(getFieldValByName("createUser", metaObject)))
            setFieldValByName("createUser", EmpThreadLocal.get().getId(), metaObject);
        if (ObjectUtil.isNull(getFieldValByName("updateTime", metaObject)))
            setFieldValByName("updateTime", EmpThreadLocal.get().getId(), metaObject);
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtil.isNull(getFieldValByName("updateTime", metaObject)))
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        if (ObjectUtil.isNull(getFieldValByName("updateTime", metaObject)))
            setFieldValByName("updateTime", EmpThreadLocal.get().getId(), metaObject);
    }
}
