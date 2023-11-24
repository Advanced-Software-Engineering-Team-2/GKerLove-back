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
    @PostMapping("like/{id}")
    public R likeSomeone(@CurrentUser User currentUser, @PathVariable("id") String userId) {
        String s = meetService.likeSomeone(currentUser, userId);
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        String r = parse.getString("ActionStatus");
        if (!r.equals("OK")) {
            throw new GKerLoveException("发送喜欢请求失败,错误信息:" + parse.getString("ErrorInfo"));
        }
        return R.ok().message("已喜欢");
    }

    @Operation(description = "发送不喜欢请求")
    @PostMapping("dislike/{id}")
    public R dislikeSomeone(@CurrentUser User currentUser, @PathVariable("id") String userId) {
        meetService.dislikeSomeone(currentUser, userId);
        return R.ok().message("取消喜欢成功");
    }

    @Operation(description = "获取用户信息")
    @GetMapping("{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok().data("user", meetService.getUserById(id));
    }

    @Operation(description = "获取我喜欢的用户列表")
    @GetMapping("likes")
    public R getMyLikes(@CurrentUser User user) {
        return R.ok().data("likes", meetService.getMyLikes(user));
    }

    @Operation(description = "获取喜欢我的用户列表")
    @GetMapping("likedBy")
    public R getLikedBy(@CurrentUser User user) {
        return R.ok().data("likedBy", meetService.getWhoLikeMe(user));
    }


}
