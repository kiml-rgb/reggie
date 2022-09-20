package com.itheima.ex;

import com.itheima.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-20 10:48:04
 */
@RestController
@Slf4j
public class ReggieExHandler {
    @ExceptionHandler(MyException.class)
    public R myEx(MyException ex) {
        if (ex.getCode() == 2000){
            // TODO:如果是2000说明是核心业务 必须要通知开发或者运维人员 短信发送
            System.out.println("发短信...");
        } else{
            log.error(ex.getMessage());
        }
        return R.error(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R ex(Exception ex) {
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
