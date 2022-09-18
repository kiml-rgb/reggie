package com.itheima.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;
import com.itheima.dto.PageDto;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    @Autowired
    private DishFlavorService dishFlavorService;

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

    @GetMapping("{id}")
    public R findDishById(@PathVariable Long id) {
        return dishService.findDishById(id);
    }

    @PutMapping
    public R updateDish(@RequestBody Dish dish) {
        return dishService.saveDish(dish);
    }

    @DeleteMapping
    public R deleteDishByIds(@RequestParam("ids") List<Long> ids) {
        return dishService.deleteDishByIds(ids);
    }

    @PostMapping("status/{statue}")
    public R setStatusByIds(@PathVariable Integer statue, @RequestParam("ids") List<Long> ids) {
        return dishService.updateStatusByIds(statue, ids);
    }

    @GetMapping("list")
    public R findDishListById(Long categoryId, Integer status) {
        List<Dish> list = dishService.list(Wrappers.lambdaQuery(Dish.class)
                .eq(Dish::getCategoryId, categoryId)
                .eq(Dish::getStatus, status));
        list.forEach(dish -> {
            List<DishFlavor> flavors = dishFlavorService.list(Wrappers.lambdaQuery(DishFlavor.class).eq(DishFlavor::getDishId, dish.getId()));
            dish.setFlavors(flavors);
        });
        return R.success(list);
    }
}
