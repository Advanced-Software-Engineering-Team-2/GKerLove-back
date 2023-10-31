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
import com.tencentyun.TLSSigAPIv2;

import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.util.HttpUtils;
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
        //在im中也注册一个用户
        TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
        String Sig = api.genUserSig("administrator", 60);
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?sdkappid=1600009914&identifier=administrator&usersig="+Sig+"&random=99999999&contenttype=json";
        JSONObject param = new JSONObject();
        param.put("UserID",user.getUsername());
        param.put("Nick",user.getUsername());
        param.put("FaceUrl","");
        String s = HttpUtils.sendPost(url, param.toJSONString());
        return R.ok().message("注册成功");
    }

    @Operation(description = "获取用户信息")
    @GetMapping("info")
    @Login
    public R getUser(@CurrentUser UserDTO userDTO) {
        return R.ok().data("user", userDTO);
    }



}

