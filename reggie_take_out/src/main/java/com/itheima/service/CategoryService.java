package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Category;
import com.itheima.vo.R;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 10:01:09
 */
public interface CategoryService extends IService<Category> {
    /**
     * 添加菜单
     * @param category
     * @return
     */
    R addCategory(Category category);

}
