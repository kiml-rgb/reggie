package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Setmeal;
import com.itheima.vo.R;

import java.util.List;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 11:06:11
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新建套餐
     * @param setmeal 套餐
     * @return R
     */
    R saveSetmeal(Setmeal setmeal);

    /**
     * 根据id查找套餐
     * @param id id
     * @return R
     */
    R findSetmealById(Long id);

    /**
     * 批量删除套餐
     * @param ids ids
     * @return R
     */
    R deleteSetmealByIds(List<Long> ids);

    /**
     * 批量修改状态
     * @param statue 状态
     * @param ids ids
     * @return R
     */
    R updateStatusByIds(Integer statue, List<Long> ids);

    /**
     * 前台查找套餐
     * @param categoryId
     * @param status
     * @return
     */
    R findSetmealList(Long categoryId, Integer status);
}
