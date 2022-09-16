package com.itheima.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Category;
import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;
import com.itheima.domain.Employee;
import com.itheima.mapper.DishMapper;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 11:06:47
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public R saveDish(Dish dish) {
        if (ObjectUtil.hasEmpty(dish.getName(), dish.getPrice(), dish.getImage(), dish.getCategoryId()))
            return R.error("参数不能为空");

        // 判断菜品是否已经存在
        Dish one = this.getOne(Wrappers.lambdaQuery(Dish.class).eq(Dish::getName, dish.getName()));
        if (ObjectUtil.isNotNull(one) && !one.getId().equals(dish.getId())) return R.error("菜品已经存在");

        if (ObjectUtil.isNotNull(dish.getId())) {
            dishFlavorService.remove(Wrappers.lambdaQuery(DishFlavor.class).eq(DishFlavor::getDishId, dish.getId()));
            this.updateById(dish);
        } else {
            this.save(dish);
        }

        if (CollUtil.isEmpty(dish.getFlavors())) return R.success(null);

        for (DishFlavor flavor : dish.getFlavors()) {
            if (ObjectUtil.hasEmpty(flavor.getName(), flavor.getValue()))
                return R.error("参数不能为空");
            flavor.setDishId(dish.getId());
            dishFlavorService.save(flavor);
        }

        return R.success("菜品新增成功");
    }

    @Override
    public R findDishById(Long id) {
        if (ObjectUtil.isNull(id)) return R.error("参数不能为空");
        Dish dish = this.getById(id);
        if (ObjectUtil.isNull(dish)) return R.error("菜品不存在");
        List<DishFlavor> list = dishFlavorService.list(Wrappers.lambdaQuery(DishFlavor.class).eq(DishFlavor::getDishId, dish.getId()));
        dish.setFlavors(list);
        return R.success(dish);
    }
}
