package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.domain.ShoppingCart;
import com.itheima.service.ShoppingCartService;
import com.itheima.utils.UserThreadLocal;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 14:34:00
 */
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("list")
    public R listShoppingCart() {
        return R.success(shoppingCartService.list(Wrappers.lambdaQuery(ShoppingCart.class).eq(ObjectUtil.isNotNull(UserThreadLocal.get()), ShoppingCart::getUserId, UserThreadLocal.get().getId())));
    }

    @PostMapping("add")
    public R addShoppingCart() {
        return null;
    }
}
