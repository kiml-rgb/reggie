package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Orders;
import com.itheima.dto.PageDto;
import com.itheima.vo.R;

import java.util.Map;

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

    /**
     * 分页查询订单
     * @param pageDto pageDto
     * @return R
     */
    R getOrderByPage(PageDto pageDto);

    /**
     * 再来一单
     * @param orders 订单
     * @return R
     */
    R getOrderAgain(Map<String, String> orders);
}
