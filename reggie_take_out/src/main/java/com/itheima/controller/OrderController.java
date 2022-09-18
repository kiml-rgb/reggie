package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.domain.Orders;
import com.itheima.dto.PageDto;
import com.itheima.service.OrdersService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("userPage")
    public R getOrderByPage(PageDto pageDto) {
        return ordersService.getOrderByPage(pageDto);
    }

    @PostMapping("again")
    public R getOrderAgain(@RequestBody Map<String, String> param) {
        return ordersService.getOrderAgain(param);

    }

}
