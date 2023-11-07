package com.gker.gkerlove.controller;

import com.gker.gkerlove.bean.Aboutme;
import com.gker.gkerlove.bean.R;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.UserDTO;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.interceptor.Login;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.AboutmeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "用户详细介绍相关接口")
@RequestMapping("/about")
public class AboutmeController {
    @Resource
    AboutmeService aboutmeService;

    @Operation(description = "填写详细介绍信息")
    @PostMapping("setabout")
    public R setabout(@RequestBody Aboutme aboutme) {
        aboutmeService.setabout(aboutme);
        return R.ok().message("信息填写成功");
    }

    @Operation(description = "获取详细介绍信息")
    @GetMapping("getabout")
    public R getabout(@RequestParam String username) {
        Aboutme aboutme = aboutmeService.getabout(username);
        return R.ok().data("about", aboutme);
    }
}
