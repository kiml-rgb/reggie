package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Dish;
import com.itheima.mapper.DishMapper;
import com.itheima.vo.R;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 11:06:11
 */
public interface DishService extends IService<Dish> {
    /**
     * 新增菜品
     * @param dish
     * @return
     */
    R saveDish(Dish dish);

    /**
     * 根据id查找id
     * @param id
     * @return
     */
    R findDishById(Long id);
}
