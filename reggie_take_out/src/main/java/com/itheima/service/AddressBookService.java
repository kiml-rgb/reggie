package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.AddressBook;
import com.itheima.vo.R;

import java.util.Map;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 10:37:08
 */
public interface AddressBookService extends IService<AddressBook> {
    /**
     * 添加地址簿
     * @param addressBook 地址簿
     * @return R
     */
    R addAddressBook(AddressBook addressBook);

    /**
     * 设置默认地址
     * @return R
     */
    R setDefaultAddressBook(Map<String, String> param);

    /**
     * 更新用户收获地址
     * @param addressBook
     * @return
     */
    R updateAddressBook(AddressBook addressBook);
}
