package com.itheima.config;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.itheima.interceptor.LoginCheckInterceptor;
import com.itheima.mapper.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 11:16:02
 */
@Configuration
@Slf4j
public class MVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器， 对登录进行拦截，指定要拦截的请求（/**）
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("消息转换器...");
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用Jackson将Java对象转为json
        converter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0, converter);
    }
}

