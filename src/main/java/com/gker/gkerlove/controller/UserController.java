package com.gker.gkerlove.controller;

import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.dto.LoginReq;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.dto.RegisterReq;
import com.gker.gkerlove.bean.dto.UpdateUserInfoReq;
import com.gker.gkerlove.bean.dto.UserDto;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.interceptor.Login;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.ImService;
import com.gker.gkerlove.service.PostService;
import com.gker.gkerlove.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;

@RestController
@Tag(name = "用户相关接口")
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @Resource
    PostService postService;

    @Resource
    ImService imService;

    @Operation(description = "登录")
    @PostMapping("login")
    public R login(@RequestBody LoginReq loginReq, HttpSession httpSession) {
        String sessionCaptcha = (String) httpSession.getAttribute("captcha");
        httpSession.removeAttribute("captcha");
        if (!loginReq.getCaptcha().equalsIgnoreCase(sessionCaptcha)) {
            throw new GKerLoveException("验证码错误");
        }
        String token = userService.login(loginReq.getUsername(), loginReq.getPassword());
        return R.ok().message("登录成功").data("token", token);
    }

    @Operation(description = "注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterReq registerReq, HttpSession httpSession) {
        String sessionCaptcha = (String) httpSession.getAttribute("captcha");
        if (!registerReq.getCaptcha().equalsIgnoreCase(sessionCaptcha)) {
            throw new GKerLoveException("验证码错误");
        }
        String s = imService.Imregister(registerReq.getUsername());
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        String r = parse.getString("ActionStatus");
        if (!r.equals("OK")) {
            throw new GKerLoveException("im系统注册失败,错误信息:" + parse.getString("ErrorInfo"));
        }
        String s1 = imService.CreateFriendGroup(registerReq.getUsername());
        JSONObject parse1 = (JSONObject) JSONObject.parse(s1);
        String r1 = parse1.getString("ActionStatus");
        if (!r1.equals("OK")) {
            throw new GKerLoveException("好友分类初始化失败,错误信息:" + parse.getString("ErrorInfo"));
        }
        httpSession.removeAttribute("captcha");
        userService.register(registerReq.getUsername(), registerReq.getPassword(), registerReq.getEmail(), registerReq.getCode());
        return R.ok().message("注册成功");
    }

    @Operation(description = "获取用户信息")
    @GetMapping("info")
    @Login
    public R getUserInfo(@CurrentUser User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return R.ok().data("user", userDto);
    }

    @Operation(description = "更新用户信息")
    @PutMapping("info")
    @Login
    public R updateInfo(@CurrentUser User user, @RequestBody UpdateUserInfoReq updateUserInfoReq) {
        userService.updateInfo(user, updateUserInfoReq);
        String s = imService.UpdateProfile(user, updateUserInfoReq);
        System.out.println(s);
        return R.ok().message("更新信息成功");
    }
}

