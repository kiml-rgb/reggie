package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Orders;
import com.itheima.vo.R;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 16:48:37
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders 订单
     * @return R
     */
    R submitOrder(Orders orders);
}
