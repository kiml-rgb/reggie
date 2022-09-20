package com.itheima.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.AddressBook;
import com.itheima.domain.OrderDetail;
import com.itheima.domain.Orders;
import com.itheima.domain.ShoppingCart;
import com.itheima.dto.PageDto;
import com.itheima.mapper.OrdersMapper;
import com.itheima.service.AddressBookService;
import com.itheima.service.OrderDetailService;
import com.itheima.service.OrdersService;
import com.itheima.service.ShoppingCartService;
import com.itheima.utils.UserThreadLocal;
import com.itheima.vo.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 16:49:56
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

    @Override
    @Transactional
    public R submitOrder(Orders orders) {
        if (ObjectUtil.isNull(orders.getAddressBookId())) return R.error("参数不合法");

        // 使用雪花算法生成一个Id
        orders.setNumber(StrUtil.toString(IdWorker.getId()));
        // 设置订单状态 默认是4
        orders.setStatus(4);
        orders.setUserId(UserThreadLocal.get().getId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());

        List<ShoppingCart> list = shoppingCartService.list(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, UserThreadLocal.get().getId()));

        // 订单总金额
        BigDecimal sum = BigDecimal.ZERO;
        for (ShoppingCart shoppingCart : list) {
            BigDecimal amount = shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber()));
            sum = sum.add(amount);
        }

        // 设置金额
        orders.setAmount(sum);

        // 查询收货人信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());

        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setUserName(UserThreadLocal.get().getName());
        orders.setConsignee(addressBook.getConsignee());

        // 订单保存
        this.save(orders);

        // 添加订单明细
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : list) {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, detail, "id");
            detail.setOrderId(orders.getId());
            orderDetails.add(detail);
        }

        orderDetailService.saveBatch(orderDetails);

        // 清空购物车
        shoppingCartService.remove(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, UserThreadLocal.get().getId()));
        return R.success(orders);
    }

    @Override
    public R getUserOrderByPage(PageDto pageDto) {
        Page<Orders> pages = this.page(new Page<>(pageDto.getPage(), pageDto.getPageSize()),
                Wrappers.lambdaQuery(Orders.class));

        List<Orders> orders = pages.getRecords();
        for (Orders order : orders) {
            // 获取订单明细
            List<OrderDetail> details = orderDetailService.list(Wrappers.lambdaQuery(OrderDetail.class).eq(OrderDetail::getOrderId, order.getId()));
            order.setOrderDetails(details);
        }

        // 修改返回数据
        pages.setRecords(orders);

        return R.success(pages);
    }

    @Override
    public R getOrderAgain(Map<String, String> param) {
        String id = param.get("id");
        if (ObjectUtil.isEmpty(id)) return R.error("参数不合法");

        // 清空原有的购物车
        shoppingCartService.remove(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, UserThreadLocal.get().getId()));

        List<OrderDetail> list = orderDetailService.list(Wrappers.lambdaQuery(OrderDetail.class).eq(OrderDetail::getOrderId, id));
        for (OrderDetail orderDetail : list) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart, "id");
            shoppingCart.setUserId(UserThreadLocal.get().getId());
            shoppingCartService.save(shoppingCart);
        }

        return R.success(null);
    }

    @Override
    public R getOrderByPage(Map<String, Object> params) {
        Integer page = Convert.convert(Integer.class, params.get("page"));
        Integer pageSize = Convert.convert(Integer.class, params.get("pageSize"));
        Long number = Convert.convert(Long.class, params.get("number"));
        LocalDateTime beginTime = Convert.convert(LocalDateTime.class, params.get("beginTime"));
        LocalDateTime endTime = Convert.convert(LocalDateTime.class, params.get("endTime"));

        if (ObjectUtil.hasEmpty(page, pageSize)) return R.error("参数不合法");

        Page<Orders> ordersPage = this.page(new Page<>(page, pageSize),
                Wrappers.lambdaQuery(Orders.class)
                        .like(ObjectUtil.isNotNull(number), Orders::getId, number)
                        .between(ObjectUtil.isAllNotEmpty(beginTime, endTime), Orders::getOrderTime, beginTime, endTime));

        return R.success(ordersPage);
    }
}
