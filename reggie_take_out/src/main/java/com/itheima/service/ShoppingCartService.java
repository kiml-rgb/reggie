package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.ShoppingCart;
import com.itheima.mapper.ShoppingCartMapper;
import com.itheima.vo.R;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 14:21:26
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 添加菜品到购物车
     * @param shoppingCart 购物车
     * @return R
     */
    R addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 减少菜品
     * @param shoppingCart 购物车
     * @return R
     */
    R subShoppingCart(ShoppingCart shoppingCart);
}
