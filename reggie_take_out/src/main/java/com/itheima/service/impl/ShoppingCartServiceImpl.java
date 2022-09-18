package com.itheima.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.ShoppingCart;
import com.itheima.mapper.ShoppingCartMapper;
import com.itheima.service.ShoppingCartService;
import com.itheima.utils.UserThreadLocal;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 14:22:03
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public R addShoppingCart(ShoppingCart shoppingCart) {
        if (ObjectUtil.hasEmpty(shoppingCart.getName(), shoppingCart.getImage(), shoppingCart.getAmount()))
            return R.error("参数不合法");

        if (ObjectUtil.isAllEmpty(shoppingCart.getDishId(), shoppingCart.getSetmealId())) return R.error("参数不合法");

        shoppingCart.setUserId(UserThreadLocal.get().getId());

        ShoppingCart one = this.getOne(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(ObjectUtil.isNotNull(shoppingCart.getSetmealId()), ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                .eq(ObjectUtil.isNotNull(shoppingCart.getDishId()), ShoppingCart::getDishId, shoppingCart.getDishId())
        );

        if (ObjectUtil.isNull(one)) {
            this.save(shoppingCart);
            return R.success(shoppingCart);
        }

        one.setNumber(one.getNumber() + 1);
        this.updateById(one);
        return R.success(one);
    }

    @Override
    public R subShoppingCart(ShoppingCart shoppingCart) {
        if (ObjectUtil.isAllEmpty(shoppingCart.getDishId(), shoppingCart.getSetmealId())) return R.error("参数不合法");

        shoppingCart.setUserId(UserThreadLocal.get().getId());

        ShoppingCart one = this.getOne(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(ObjectUtil.isNotNull(shoppingCart.getSetmealId()), ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                .eq(ObjectUtil.isNotNull(shoppingCart.getDishId()), ShoppingCart::getDishId, shoppingCart.getDishId())
        );

        one.setNumber(one.getNumber() - 1);
        if (one.getNumber() == 0) {
            this.removeById(one);
        }

        this.updateById(one);
        return R.success(one);
    }
}
