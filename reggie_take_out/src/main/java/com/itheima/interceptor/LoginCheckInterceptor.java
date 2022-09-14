package com.itheima.interceptor;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.itheima.domain.Employee;
import com.itheima.utils.EmpThreadLocal;
import com.itheima.vo.R;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断本次请求是否需要拦截

        if (!(handler instanceof HandlerMethod)) return true;

        if (request.getRequestURI().contains("employee/login")) return true;

        if (ObjectUtil.isNotNull(request.getSession().getAttribute("employee"))) {
            EmpThreadLocal.set((Employee) request.getSession().getAttribute("employee"));
            return true;
        }

        // 设置状态码401
        response.setStatus(401);

        response.setContentType("application/json;charset=utf-8");

        response.getWriter().write(JSONUtil.toJsonStr(R.error("用户未登录")));

        // 判断是否登录，没有直接返回401
        return false;
    }

    /**
     * 请求回到浏览器之前执行
     * @param request request
     * @param response response
     * @param handler handler
     * @param modelAndView modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        EmpThreadLocal.remove();
    }
}
