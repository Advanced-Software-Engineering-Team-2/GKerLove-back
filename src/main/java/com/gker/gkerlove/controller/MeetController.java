package com.gker.gkerlove.controller;

import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.dto.UserDto;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.MeetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "遇见相关接口")
@RequestMapping("/meet")
public class MeetController {
    @Resource
    MeetService meetService;

    @Operation(description = "获取遇见用户列表")
    @GetMapping
    public R getUserList(@CurrentUser User user, @RequestParam(required = false) String gender, @RequestParam(required = false) Integer minAge, @RequestParam(required = false) Integer maxAge, @RequestParam(required = false) String city, @RequestParam(required = false) String institute) {
        List<UserDto> userList = meetService.getUserList(user, gender, minAge, maxAge, city, institute);
        return R.ok().data("userList", userList);
    }


    @Operation(description = "发送喜欢请求")
    @PostMapping("addlove")
    public R addlove(@RequestParam String fromusername, @RequestParam String tousername) {
        String s = meetService.MeetingAddlover(fromusername, tousername);
        meetService.MeetingNotlove(fromusername, tousername);
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        String r = parse.getString("ActionStatus");
        if (!r.equals("OK")) {
            throw new GKerLoveException("发送喜欢请求失败,错误信息:" + parse.getString("ErrorInfo"));
        }
        return R.ok().message("已发送喜欢请求");
    }

    @Operation(description = "发送不喜欢请求")
    @PostMapping("notlove")
    public R notlove(@RequestParam String fromusername, @RequestParam String tousername) {
        meetService.MeetingNotlove(fromusername, tousername);
        return R.ok().message("已不再推荐该用户");
    }

    @Operation(description = "获取用户信息")
    @GetMapping("{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok().data("user", meetService.getUserById(id));
    }

}
