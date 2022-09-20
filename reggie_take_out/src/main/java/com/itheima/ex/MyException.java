package com.itheima.ex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyf
 * @program: bank
 * @description: 自定义异常
 * @date 2022-09-06 16:02:12
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class  MyException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 描述异常的信息
     */
    private String message;
}