package com.itheima.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.AddressBook;
import com.itheima.mapper.AddressBookMapper;
import com.itheima.service.AddressBookService;
import com.itheima.utils.UserThreadLocal;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 10:40:48
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public R addAddressBook(AddressBook addressBook) {
        if (ObjectUtil.hasEmpty(addressBook.getConsignee(),
                addressBook.getSex(), addressBook.getPhone())) return R.error("参数不合法");

        // 获取当前用户id
        addressBook.setUserId(UserThreadLocal.get().getId());
        this.save(addressBook);

        redisTemplate.delete("reggie_addressBook_uid_" + UserThreadLocal.get().getId());

        return R.success(addressBook);
    }

    @Override
    public R setDefaultAddressBook(Map<String, String> param) {
        String id = param.get("id");
        if (ObjectUtil.isEmpty(id)) return R.error("参数不合法");

        // 一个用户只能有一个默认
        AddressBook addressBook = this.getOne(Wrappers.lambdaQuery(AddressBook.class).eq(AddressBook::getIsDefault, 1)
                .eq(AddressBook::getUserId, UserThreadLocal.get().getId()));

        if (ObjectUtil.isNotNull(addressBook)) {
            addressBook.setIsDefault(0);
            this.updateById(addressBook);
        }

        this.update(Wrappers.lambdaUpdate(AddressBook.class).eq(AddressBook::getId, id).set(AddressBook::getIsDefault, 1));

        redisTemplate.delete("reggie_addressBook_uid_" + UserThreadLocal.get().getId());

        return R.success(null);
    }

    @Override
    public R updateAddressBook(AddressBook addressBook) {
        if (ObjectUtil.hasEmpty(addressBook.getConsignee(),
                addressBook.getSex(), addressBook.getPhone())) return R.error("参数不合法");

        this.updateById(addressBook);

        redisTemplate.delete("reggie_addressBook_uid_" + UserThreadLocal.get().getId());

        return R.success(null);
    }

    @Override
    public R listAddressBook() {
        /* Redis中的Key的定义 在实际开发中有着严格的要求 */
        /* Redis中存储的数据通常会有两种 */
        /* 1.  业务中的临时性数据  项目名称:模块名称:业务名称:唯一标识 值 */
        /*     手机验证码:        regiee:user:userlogin:1341111111 234533*/
        /* 2.  数据库中的缓存数据  数据库名称:表名称:主键名称:主键值 值 */
        /*     用户收货地址:      regiee:addressbook:uid:15 [{},{},{}]  */

        /* Redis中已经缓存了个人的地址 直接返回即可 */
        String addressBook = redisTemplate.opsForValue().get("reggie_addressBook_uid_" + UserThreadLocal.get().getId());
        /* 把JSON字符串数据反序列化成List集合 */
        List<AddressBook> addressBooks = JSONUtil.toList(addressBook, AddressBook.class);
        if (CollUtil.isNotEmpty(addressBooks)) return R.success(addressBooks);

        List<AddressBook> list = this.list(Wrappers.lambdaQuery(AddressBook.class)
                .eq(ObjectUtil.isNotNull(UserThreadLocal.get().getId()), AddressBook::getUserId, UserThreadLocal.get().getId()));
        // redis数据库存入数据，一般情况下都会设置生存时间
        redisTemplate.opsForValue()
                .set("reggie_addressBook_uid_" + UserThreadLocal.get().getId(), JSONUtil.toJsonStr(list),
                        30, TimeUnit.DAYS);
        return R.success(list);
    }
}
