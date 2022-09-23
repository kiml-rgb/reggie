package com.itheima.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Category;
import com.itheima.domain.Dish;
import com.itheima.domain.Setmeal;
import com.itheima.dto.PageDto;
import com.itheima.mapper.CategoryMapper;
import com.itheima.service.CategoryService;
import com.itheima.service.DishService;
import com.itheima.service.SetmealService;
import com.itheima.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-14 10:03:13
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private SetmealService setMealService;

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public R addCategory(Category category) {
        if (ObjectUtil.isNull(category) || ObjectUtil.hasEmpty(category.getName(), category.getType(), category.getSort()))
            return R.error("参数不能为空");
        Category one = this.getOne(Wrappers.lambdaQuery(Category.class).eq(Category::getName, category.getName()));
        if (ObjectUtil.isNotNull(one)) return R.error(category.getType() == 1 ? "分类已经存在" : "套餐已经存在");

        if (this.save(category)) {
            // 删除redis中的缓存
            // redisTemplate.delete("reggie_category_categorylist_" + category.getType());
            redisTemplate.delete("reggie_category_categorylist");
            return R.success(null);
        }

        return R.error("新增失败,请稍后重试");
    }

    @Override
    public R deleteCategory(Long id) {
        if (ObjectUtil.isNull(id)) return R.error("参数不能为空");

        List<Dish> dishList = dishService.list(Wrappers.lambdaQuery(Dish.class).eq(Dish::getCategoryId, id));
        List<Setmeal> setmealList = setMealService.list(Wrappers.lambdaQuery(Setmeal.class).eq(Setmeal::getCategoryId, id));

        if (CollUtil.isNotEmpty(dishList) || CollUtil.isNotEmpty(setmealList)) return R.error("该分类被菜品关联，不能删除");

        Category category = this.getById(id);
        if (this.removeById(id)) {
            // 删除redis中的缓存
            // redisTemplate.delete(redisTemplate.keys("reggie_category_categorylist_" + category.getType()));
            redisTemplate.delete(redisTemplate.keys("reggie_category_categorylist"));
            return R.success(null);
        }
        return R.error("删除失败，请稍后重试");
    }

    @Override
    public R updateCategory(Category category) {
        if (ObjectUtil.hasEmpty(category.getId(), category.getSort(), category.getName()))
            return R.error("参数不能为空");

        Category old = this.getById(category.getId());
        if (old.getName().equals(category.getName())) {
            Category one = this.getOne(Wrappers.lambdaQuery(Category.class).eq(Category::getName, category.getName()));
            if (ObjectUtil.isNotNull(one)) return R.error("分类已经存在");
        }

        if (this.updateById(category)) {
            // 删除redis中的缓存
            // redisTemplate.delete(redisTemplate.keys("reggie_category_categorylist_*"));
            redisTemplate.delete(redisTemplate.keys("reggie_category_categorylist"));
            return R.success(null);
        }
        return R.error("更新失败,请稍后重试");
    }

    @Override
    public R findCategory(Integer type) {
        // 设置type的默认值， 1表示菜品
        // if (ObjectUtil.isNotNull(type) && type <= 0) type = 1;

        // Redis中已经缓存，直接返回
        // String jsonList = redisTemplate.opsForValue().get("reggie_category_categorylist_" + type);
        // List<Category> categories = JSONUtil.toList(jsonList, Category.class);
        // if (CollUtil.isNotEmpty(categories)) return R.success(categories);

        String jsonList = redisTemplate.opsForValue().get("reggie_category_categorylist");
        List<Category> categories = JSONUtil.toList(jsonList, Category.class);
        if (CollUtil.isNotEmpty(categories)) {
            return R.success(ObjectUtil.isNotNull(type) ? categories.stream()
                    // 如果type不为空 添加筛选条件 type
                    .filter(category -> category.getType().equals(type))
                    .collect(Collectors.toList()) : categories);
        }


        List<Category> list = this.list(Wrappers.lambdaQuery(Category.class)
                // .eq(ObjectUtil.isNotNull(type), Category::getType, type)
                .orderByAsc(Category::getType)
                .orderByAsc(Category::getSort));

        // 把菜单存入redis
        // if (CollUtil.isNotEmpty(list))
        //     redisTemplate.opsForValue().set("reggie_category_categorylist_" + type,
        //             JSONUtil.toJsonStr(list),
        //             30, TimeUnit.DAYS);

        if (CollUtil.isNotEmpty(list))
            redisTemplate.opsForValue().set("reggie_category_categorylist",
                    JSONUtil.toJsonStr(list),
                    30, TimeUnit.DAYS);

        return R.success(list);
    }

    @Override
    public R findCategoryByPage(PageDto pageDto) {
        pageDto.check();

        // Redis中已经缓存，直接返回
//        String jsonList = redisTemplate.opsForValue().get("reggie_category_type_" + type);
//        List<Category> categories = JSONUtil.toList(jsonList, Category.class);
//        if (CollUtil.isNotEmpty(categories)) return R.success(categories);

        return R.success(this.page(new Page<>(pageDto.getPage(), pageDto.getPageSize())));
    }
}
