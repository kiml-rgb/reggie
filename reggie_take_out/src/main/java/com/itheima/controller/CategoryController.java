package com.itheima.controller;

import com.itheima.domain.Category;
import com.itheima.service.CategoryService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
