package com.itheima.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.domain.AddressBook;
import com.itheima.service.AddressBookService;
import com.itheima.utils.UserThreadLocal;
import com.itheima.vo.R;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-18 10:34:15
 */
@RestController
@RequestMapping("addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R addAddressBook(@RequestBody AddressBook addressBook) {
        return addressBookService.addAddressBook(addressBook);
    }

    @GetMapping("list")
    public R listAddressBook() {
        return R.success(addressBookService.list(Wrappers.lambdaQuery(AddressBook.class).eq(ObjectUtil.isNotNull(UserThreadLocal.get().getId()), AddressBook::getUserId, UserThreadLocal.get().getId())));
    }

    @PutMapping("default")
    public R setDefaultAddressBook(@RequestBody Map<String, String> param) {
        return addressBookService.setDefaultAddressBook(param);
    }

    @GetMapping("default")
    public R getDefaultAddressBook() {
        return R.success(addressBookService.getOne(Wrappers.lambdaQuery(AddressBook.class)
        .eq(AddressBook::getUserId, UserThreadLocal.get().getId())
        .eq(AddressBook::getIsDefault, 1)));
    }

    @GetMapping("{id}")
    public R getAddressBookById(@PathVariable Long id) {
        return R.success(addressBookService.getById(id));
    }

    @PutMapping
    public R updateAddressBook(@RequestBody AddressBook addressBook) {
        return addressBookService.updateAddressBook(addressBook);
    }

    @DeleteMapping
    public R deleteAddressBook(Long ids) {
        return R.success(addressBookService.removeById(ids));
    }

}
