package com.itheima.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;
import com.itheima.mapper.DishMapper;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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

        redisTemplate.delete("reggie_dish_categoryId_" + dish.getCategoryId());

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

    @Override
    @Transactional
    public R deleteDishByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) return R.error("参数不合法");

        // 删除缓存
        this.listByIds(ids).forEach(dish -> redisTemplate.delete("reggie_dish_categoryId_" + dish.getCategoryId()));

        // 删除关联的口味
        ids.forEach(id -> dishFlavorService.remove(Wrappers.lambdaQuery(DishFlavor.class)
                .eq(DishFlavor::getDishId, id)));

        this.removeByIds(ids);

        return R.success(null);
    }

    @Override
    @Transactional
    public R updateStatusByIds(Integer status, List<Long> ids) {
        if (!(ObjectUtil.isNotEmpty(status) && (status == 0 || status == 1)))
            return R.error("参数不合法");

        if (CollUtil.isEmpty(ids)) return R.error("参数不合法");

        for (Long id : ids) {
            Dish dish = this.getById(id);
            dish.setStatus(status);
            if (!this.updateById(dish)) {
                return R.error("修改失败");
            }
        }

        // 删除所有缓存
        redisTemplate.delete(redisTemplate.keys("reggie_dish_categoryId_*"));

        return R.success(null);
    }

    @Override
    public R findDishList(Long categoryId, Integer status) {
        if (ObjectUtil.hasEmpty(categoryId, status)) return R.error("参数不合法");

        // 从缓存中查找list
        String jsonList = redisTemplate.opsForValue().get("reggie_dish_categoryId_" + categoryId);
        if (ObjectUtil.isNotEmpty(jsonList)) return R.success(JSONUtil.toList(jsonList, Dish.class));

        List<Dish> list = this.list(Wrappers.lambdaQuery(Dish.class)
                .eq(Dish::getCategoryId, categoryId)
                .eq(Dish::getStatus, status));
        list.forEach(dish -> {
            List<DishFlavor> flavors = dishFlavorService.list(Wrappers.lambdaQuery(DishFlavor.class).eq(DishFlavor::getDishId, dish.getId()));
            dish.setFlavors(flavors);
        });

        // 存入缓存
        redisTemplate.opsForValue().set("reggie_dish_categoryId_" + categoryId, JSONUtil.toJsonStr(list));
        return R.success(list);
    }
}
