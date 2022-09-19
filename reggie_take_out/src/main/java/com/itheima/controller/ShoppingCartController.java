package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.domain.ShoppingCart;
import com.itheima.service.ShoppingCartService;
import com.itheima.utils.UserThreadLocal;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
    public R listShoppingCart(HttpSession session) {
        session.getAttribute("user");
        return R.success(shoppingCartService.list(Wrappers.lambdaQuery(ShoppingCart.class).eq(ObjectUtil.isNotNull(UserThreadLocal.get()), ShoppingCart::getUserId, UserThreadLocal.get().getId())));
    }

    @PostMapping("add")
    public R addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.addShoppingCart(shoppingCart);
    }

    @PostMapping("sub")
    public R subShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.subShoppingCart(shoppingCart);
    }

    @DeleteMapping("clean")
    public R cleanShoppingCart() {
        return shoppingCartService.cleanShoppingCart();
    }
}
