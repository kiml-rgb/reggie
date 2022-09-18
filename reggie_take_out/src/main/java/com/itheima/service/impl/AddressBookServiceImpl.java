package com.itheima.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.AddressBook;
import com.itheima.mapper.AddressBookMapper;
import com.itheima.service.AddressBookService;
import com.itheima.utils.UserThreadLocal;
import com.itheima.vo.R;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 10:40:48
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    public R addAddressBook(AddressBook addressBook) {
        if (ObjectUtil.hasEmpty(addressBook.getConsignee(),
                addressBook.getSex(), addressBook.getPhone())) return R.error("参数不合法");

        // 获取当前用户id
        addressBook.setUserId(UserThreadLocal.get().getId());
        this.save(addressBook);
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

        return R.success(null);
    }

    @Override
    public R updateAddressBook(AddressBook addressBook) {
        if (ObjectUtil.hasEmpty(addressBook.getConsignee(),
                addressBook.getSex(), addressBook.getPhone())) return R.error("参数不合法");

        this.updateById(addressBook);

        return R.success(null);
    }
}
