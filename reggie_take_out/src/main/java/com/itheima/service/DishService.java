package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Dish;
import com.itheima.mapper.DishMapper;
import com.itheima.vo.R;

import java.util.List;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 11:06:11
 */
public interface DishService extends IService<Dish> {
    /**
     * 新增菜品
     * @param dish 菜品
     * @return R
     */
    R saveDish(Dish dish);

    /**
     * 根据id查找id
     * @param id id
     * @return R
     */
    R findDishById(Long id);

    /**
     * 根据id删除
     * @param ids ids
     * @return R
     */
    R deleteDishByIds(List<Long> ids);

    /**
     * 根据id修改停售状态
     * @param status 是否停售
     * @param ids ids
     * @return R
     */
    R updateStatusByIds(Integer status, List<Long> ids);

    /**
     * 前台查找菜品
     * @param categoryId categoryId
     * @param status status
     * @return R
     */
    R findDishList(Long categoryId, Integer status);
}
