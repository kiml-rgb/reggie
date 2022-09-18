package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Orders;
import com.itheima.mapper.OrdersMapper;
import com.itheima.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 16:49:56
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
