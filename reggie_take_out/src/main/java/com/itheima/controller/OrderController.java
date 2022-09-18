package com.itheima.controller;

import com.itheima.domain.Orders;
import com.itheima.service.OrdersService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 16:40:48
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("submit")
    public R submitOrder(@RequestBody Orders orders) {
        return ordersService.submitOrder(orders);
    }


}
