package com.gker.gkerlove.controller;

import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.dto.PostDto;
import com.gker.gkerlove.interceptor.Login;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "动态相关接口")
@RequestMapping("/post")
@Login
public class PostController {
    @Resource
    PostService postService;

    @Operation(description = "发布动态")
    @PostMapping
    public R addPost(@CurrentUser User user, @RequestBody PostDto postDto) {
        postService.addPost(user, postDto);
        return R.ok().message("发布成功");
    }
}