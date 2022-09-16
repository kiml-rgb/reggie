package com.itheima.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.domain.Dish;
import com.itheima.dto.PageDto;
import com.itheima.service.DishService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-16 14:59:05
 */
@RestController
@RequestMapping("dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public R saveDish(@RequestBody Dish dish) throws IOException {
        return dishService.saveDish(dish);
    }

    @GetMapping("page")
    public R findEmployeeByPage(PageDto pageDto) {
        pageDto.check();
        return R.success(dishService.page(new Page<>(pageDto.getPage(), pageDto.getPageSize()),
                Wrappers.lambdaQuery(Dish.class)
                        .like(StrUtil.isNotEmpty(pageDto.getName()), Dish::getName, pageDto.getName())));
    }


}
