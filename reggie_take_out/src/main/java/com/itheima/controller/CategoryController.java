package com.itheima.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.domain.Category;
import com.itheima.dto.PageDto;
import com.itheima.service.CategoryService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 09:56:59
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @GetMapping("/page")
    public R findCategoryByPage(PageDto pageDto) {
        pageDto.check();
        return R.success(categoryService.page(new Page<>(pageDto.getPage(), pageDto.getPageSize())));
    }

    @DeleteMapping
    public R deleteCategory(Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("{id}")
    public R findCategoryById(@PathVariable Long id) {
        return R.success(categoryService.getById(id));
    }

    @PutMapping
    public R updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @GetMapping("/list")
    public R findCategory(Integer type) {
        if (ObjectUtil.isNull(type) || type <= 0) type = 1;
        return R.success(categoryService.list(Wrappers.lambdaQuery(Category.class).eq(Category::getType, type)));
    }
}
