package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Dish;
import com.itheima.domain.Setmeal;
import com.itheima.mapper.DishMapper;
import com.itheima.mapper.SetMealMapper;
import com.itheima.service.DishService;
import com.itheima.service.SetMealService;
import org.springframework.stereotype.Service;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 11:06:47
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
}
