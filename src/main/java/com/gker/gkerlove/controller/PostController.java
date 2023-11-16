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
import org.springframework.web.bind.annotation.*;

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
        postDto = postService.addPost(user, postDto);
        return R.ok().message("发布成功").data("post", postDto);
    }

    @Operation(description = "获取用户发布的动态")
    @GetMapping("{id}")
    public R getUserPostList(@PathVariable("id") String id) {
        return R.ok().data("posts", postService.retrieve(null, null, id));
    }
}