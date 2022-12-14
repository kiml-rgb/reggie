package com.itheima.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public R submitOrder(Orders orders) {
        if (ObjectUtil.isNull(orders.getAddressBookId())) return R.error("???????????????");

        // ??????????????????????????????Id
        orders.setNumber(StrUtil.toString(IdWorker.getId()));
        // ?????????????????? ?????????4
        orders.setStatus(4);
        orders.setUserId(UserThreadLocal.get().getId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());

        List<ShoppingCart> list = shoppingCartService.list(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, UserThreadLocal.get().getId()));

        // ???????????????
        BigDecimal sum = BigDecimal.ZERO;
        for (ShoppingCart shoppingCart : list) {
            BigDecimal amount = shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber()));
            sum = sum.add(amount);
        }

        // ????????????
        orders.setAmount(sum);

        // ?????????????????????
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());

        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setUserName(UserThreadLocal.get().getName());
        orders.setConsignee(addressBook.getConsignee());

        // ????????????
        this.save(orders);

        // ??????????????????
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : list) {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, detail, "id");
            detail.setOrderId(orders.getId());
            orderDetails.add(detail);
        }

        orderDetailService.saveBatch(orderDetails);

        redisTemplate.delete("reggie_orders_uid_" + UserThreadLocal.get().getId());

        // ???????????????
        shoppingCartService.remove(Wrappers.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, UserThreadLocal.get().getId()));
        return R.success(orders);
    }

    @Override
    public R getUserOrderByPage(PageDto pageDto){
        pageDto.check();

        // ????????????
        // String jsonList = redisTemplate.opsForValue().get("reggie_orders_uid_" + UserThreadLocal.get().getId());
        // List<Orders> list = JSONUtil.toList(jsonList, Orders.class);
        // if (CollUtil.isNotEmpty(list)) return R.success(new Page<Orders>(pageDto.getPage(), pageDto.getPageSize()).setRecords(list));

        Page<Orders> pages = this.page(new Page<>(pageDto.getPage(), pageDto.getPageSize()),
                Wrappers.lambdaQuery(Orders.class)
                        .eq(Orders::getUserId, UserThreadLocal.get().getId())
                        .orderByDesc(Orders::getOrderTime));

        List<Orders> orders = pages.getRecords();
        for (Orders order : orders) {
            // ??????????????????
            List<OrderDetail> details = orderDetailService.list(Wrappers.lambdaQuery(OrderDetail.class).eq(OrderDetail::getOrderId, order.getId()));
            order.setOrderDetails(details);
        }

        pages.setRecords(orders);
        // redisTemplate.opsForValue().set("reggie_orders_uid_" + UserThreadLocal.get().getId(),
        //         JSONUtil.toJsonStr(orders),
        //         30, TimeUnit.DAYS);
        
        return R.success(pages);
    }

    @Override
    public R getOrderAgain(Map<String, String> param) {
        String id = param.get("id");
        if (ObjectUtil.isEmpty(id)) return R.error("???????????????");

        // ????????????????????????
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

        if (ObjectUtil.hasEmpty(page, pageSize)) return R.error("???????????????");

        Page<Orders> ordersPage = this.page(new Page<>(page, pageSize),
                Wrappers.lambdaQuery(Orders.class)
                        .like(ObjectUtil.isNotNull(number), Orders::getId, number)
                        .between(ObjectUtil.isAllNotEmpty(beginTime, endTime), Orders::getOrderTime, beginTime, endTime));

        return R.success(ordersPage);
    }
}
