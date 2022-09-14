package com.itheima.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Category;
import com.itheima.mapper.CategoryMapper;
import com.itheima.service.CategoryService;
import com.itheima.vo.R;
import org.springframework.stereotype.Service;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 10:03:13
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public R addCategory(Category category) {
        if (ObjectUtil.isNull(category) || ObjectUtil.hasEmpty(category.getName(), category.getType(), category.getSort()))
            return R.error("参数不能为空");
        Category one = this.getOne(Wrappers.lambdaQuery(Category.class).eq(Category::getName, category.getName()));
        if (ObjectUtil.isNotNull(one)) return R.error(category.getType() == 1 ? "分类已经存在" : "套餐已经存在");

        if (this.save(category)) return R.success(category);

        return R.error("新增失败,请稍后重试");
    }
}
