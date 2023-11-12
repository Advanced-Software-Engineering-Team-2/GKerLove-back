package com.gker.gkerlove.interceptor;

import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.UserService;
import com.gker.gkerlove.util.JwtUtils;
import com.gker.gkerlove.util.ResponseUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String token = request.getHeader("token");

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Method method = handlerMethod.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();


        // 需要登录权限
        if (declaringClass.isAnnotationPresent(Login.class) || method.isAnnotationPresent(Login.class)) {
            if (!StringUtils.hasLength(token)) {
                ResponseUtil.out(response, R.error().code(R.ResultCode.UNAUTHENTICATED).message("未登录"));
                return false;
            }
            if (!JwtUtils.checkToken(token)) {
                ResponseUtil.out(response, R.error().code(R.ResultCode.UNAUTHENTICATED).message("登录失效"));
                return false;
            }

        }

        // 判断是否需要将用户信息注入controller方法中，当有使用@CurrentUser注解的参数时，才进行注入
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == CurrentUser.class) {
                    User user = userService.getById(JwtUtils.getUserId(token));
                    request.setAttribute("user", user);
                    return true;
                }
            }
        }
        return true;
    }
}
