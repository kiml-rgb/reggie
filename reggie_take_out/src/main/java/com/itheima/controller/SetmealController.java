package com.itheima.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.domain.Dish;
import com.itheima.domain.Setmeal;
import com.itheima.dto.PageDto;
import com.itheima.service.CategoryService;
import com.itheima.service.SetmealService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-16 14:59:05
 */
@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R saveSetmeal(@RequestBody Setmeal setmeal) throws IOException {
        return setmealService.saveSetmeal(setmeal);
    }

    @GetMapping("page")
    public R findSetmealByPage(PageDto pageDto) {
        pageDto.check();
        Page<Setmeal> page = setmealService.page(new Page<>(pageDto.getPage(), pageDto.getPageSize()),
                Wrappers.lambdaQuery(Setmeal.class)
                        .like(StrUtil.isNotEmpty(pageDto.getName()), Setmeal::getName, pageDto.getName()));

        // 增加套餐分类
        List<Setmeal> setmealList = page.getRecords().stream()
                .peek(setmeal -> setmeal.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName()))
                .collect(Collectors.toList());

        page.setRecords(setmealList);

        return R.success(page);
    }

    @GetMapping("{id}")
    public R findSetmealById(@PathVariable Long id) {
        return setmealService.findSetmealById(id);
    }

    @PutMapping
    public R updateSetmeal(@RequestBody Setmeal setmeal) {
        return setmealService.saveSetmeal(setmeal);
    }

    @DeleteMapping
    public R deleteSetmealByIds(@RequestParam("ids") List<Long> ids) {
        return setmealService.deleteSetmealByIds(ids);
    }

    @PostMapping("status/{statue}")
    public R setStatusByIds(@PathVariable Integer statue, @RequestParam("ids") List<Long> ids) {
        return setmealService.updateStatusByIds(statue, ids);
    }

    @GetMapping("list")
    public R findSetmealList(Long categoryId, Integer status) {
        return setmealService.findSetmealList(categoryId, status);
    }
}
