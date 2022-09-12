package com.itheima.dto;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 15:51:29
 */
@Data
public class EmpPageDto {
    private String name;
    private Integer page;
    private Integer pageSize;

    // 校验Page，PageSize
    public void check() {
        if (ObjectUtil.isEmpty(this.page)) this.page = 1;
        if (ObjectUtil.isEmpty(this.pageSize)) this.pageSize = 10;
    }
}
