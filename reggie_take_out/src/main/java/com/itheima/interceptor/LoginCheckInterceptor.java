package com.itheima.interceptor;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.itheima.vo.R;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-12 10:45:58
 */
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断本次请求是否需要拦截

        // 通过匹配路径
        // String requestURI = request.getRequestURI();

        /*String[] urls = new String[] {"/employee/login", "/employee/logout", "/backend/**", "/front/**"};

        if (check(urls, requestURI)) {
            return true;
        }*/

        if (!(handler instanceof HandlerMethod)) return true;

        if (request.getRequestURI().contains("employee/login")) return true;

        if (ObjectUtil.isNotNull(request.getSession().getAttribute("employee"))) return true;

        response.setStatus(401);

        response.setContentType("application/json;charset=utf-8");

        response.getWriter().println(JSONUtil.toJsonStr(R.error("用户未登录")));

        // 判断是否登录，没有直接返回401
        return false;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
