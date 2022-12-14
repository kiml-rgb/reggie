package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Category;
import com.itheima.dto.PageDto;
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
     * @param category 菜单
     * @return R
     */
    R addCategory(Category category);

    /**
     * 删除菜单
     *
     * @param id id
     * @return R
     */
    R deleteCategory(Long id);

    /**
     * 修改套餐
     * @param category category
     * @return R
     */
    R updateCategory(Category category);

    /**
     * 根据类型查找菜单
     * @param type type
     * @return R
     */
    R findCategory(Integer type);

    /**
     * 分页查询菜单
     * @param pageDto pageDto
     * @return
     */
    R findCategoryByPage(PageDto pageDto);
}
