package com.swzl.interceptors;

import com.swzl.entity.User;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Object loginUser = null;
        try {
            loginUser = request.getSession().getAttribute("loginUser");
            if (null != loginUser) {
                if (loginUser instanceof User) {
                    User user = (User) loginUser;
                    if (!StringUtils.isEmpty(user.getUsername())) {
                        //有登录用户的用户名信息就放行
                        return true;
                    }
                }
            }
            //没有登录凭证
            response.sendRedirect("http://localhost:8080/login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
