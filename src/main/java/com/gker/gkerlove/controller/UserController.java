package com.gker.gkerlove.controller;

import com.gker.gkerlove.bean.R;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.UserDTO;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.interceptor.Login;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "用户相关接口")
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @Operation(description = "登录")
    @PostMapping("login")
    public R login(@RequestBody User user, @RequestParam String captcha, HttpSession httpSession) {
        String sessionCaptcha = (String) httpSession.getAttribute("captcha");
        httpSession.removeAttribute("captcha");
        if (!captcha.equalsIgnoreCase(sessionCaptcha)) {
            throw new GKerLoveException("验证码错误");
        }
        String token = userService.login(user);
        return R.ok().data("token", token);
    }

    @Operation(description = "注册")
    @PostMapping("register")
    public R register(@RequestBody User user, @RequestParam String captcha, @RequestParam String code, HttpSession httpSession) {
        String sessionCaptcha = (String) httpSession.getAttribute("captcha");
        if (!captcha.equalsIgnoreCase(sessionCaptcha)) {
            throw new GKerLoveException("验证码错误");
        }
        httpSession.removeAttribute("captcha");
        userService.register(user, code);
        return R.ok().message("注册成功");
    }

    @Operation(description = "获取用户信息")
    @GetMapping("info")
    @Login
    public R getUser(@CurrentUser UserDTO userDTO) {
        return R.ok().data("user", userDTO);
    }


}