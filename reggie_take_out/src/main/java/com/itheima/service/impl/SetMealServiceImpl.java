package com.itheima.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Setmeal;
import com.itheima.domain.SetmealDish;
import com.itheima.mapper.SetmealMapper;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
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
public class SetMealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public R saveSetmeal(Setmeal setmeal) {
        if (ObjectUtil.hasEmpty(setmeal.getName(), setmeal.getPrice(), setmeal.getImage(), setmeal.getCategoryId()))
            return R.error("参数不能为空");

        if (CollUtil.isEmpty(setmeal.getSetmealDishes())) return R.error("参数不能为空");

        // 判断套餐是否已经存在
        Setmeal one = this.getOne(Wrappers.lambdaQuery(Setmeal.class).eq(Setmeal::getName, setmeal.getName()));
        if (ObjectUtil.isNotNull(one) && !one.getId().equals(setmeal.getId())) return R.error("套餐已经存在");

        if (ObjectUtil.isNotNull(setmeal.getId())) {
            setmealDishService.remove(Wrappers.lambdaQuery(SetmealDish.class).eq(SetmealDish::getDishId, setmeal.getId()));
            this.updateById(setmeal);
        } else {
            this.save(setmeal);
        }

        for (SetmealDish setmealDish : setmeal.getSetmealDishes()) {
            if (ObjectUtil.hasEmpty(setmealDish.getName()))
                return R.error("参数不能为空");
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishService.save(setmealDish);
        }

        return R.success(null);
    }

    @Override
    public R findSetmealById(Long id) {
        if (ObjectUtil.isNull(id)) return R.error("参数不能为空");
        Setmeal setmeal = this.getById(id);
        if (ObjectUtil.isNull(setmeal)) return R.error("套餐不存在");
        List<SetmealDish> list = setmealDishService.list(Wrappers.lambdaQuery(SetmealDish.class).eq(SetmealDish::getSetmealId, setmeal.getId()));
        setmeal.setSetmealDishes(list);
        return R.success(setmeal);
    }

    @Override
    @Transactional
    public R deleteSetmealByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) return R.error("参数不合法");

        // 删除关联的菜品
        ids.forEach(id -> setmealDishService.remove(Wrappers.lambdaQuery(SetmealDish.class)
                .eq(SetmealDish::getSetmealId, id)));

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
            Setmeal setmeal = this.getById(id);
            setmeal.setStatus(status);
            if (!this.updateById(setmeal)) {
                return R.error("修改失败");
            }
        }

        return R.success(null);
    }
}
