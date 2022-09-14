package com.itheima.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Category;
import com.itheima.domain.Dish;
import com.itheima.domain.Setmeal;
import com.itheima.dto.PageDto;
import com.itheima.mapper.CategoryMapper;
import com.itheima.service.CategoryService;
import com.itheima.service.DishService;
import com.itheima.service.SetMealService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 10:03:13
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private SetMealService setMealService;

    @Autowired
    private DishService dishService;

    @Override
    public R addCategory(Category category) {
        if (ObjectUtil.isNull(category) || ObjectUtil.hasEmpty(category.getName(), category.getType(), category.getSort()))
            return R.error("参数不能为空");
        Category one = this.getOne(Wrappers.lambdaQuery(Category.class).eq(Category::getName, category.getName()));
        if (ObjectUtil.isNotNull(one)) return R.error(category.getType() == 1 ? "分类已经存在" : "套餐已经存在");

        if (this.save(category)) return R.success(null);

        return R.error("新增失败,请稍后重试");
    }

    @Override
    public R deleteCategory(Long id) {
        if (ObjectUtil.isNull(id)) return R.error("参数不能为空");

        List<Dish> dishList = dishService.list(Wrappers.lambdaQuery(Dish.class).eq(Dish::getCategoryId, id));
        List<Setmeal> setmealList = setMealService.list(Wrappers.lambdaQuery(Setmeal.class).eq(Setmeal::getCategoryId, id));

        if (CollUtil.isNotEmpty(dishList) || CollUtil.isNotEmpty(setmealList)) return R.error("该分类被菜品关联，不能删除");
        if (this.removeById(id)) return R.success(null);
        return R.error("删除失败，请稍后重试");
    }
}

