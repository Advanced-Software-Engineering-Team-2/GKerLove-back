package com.gker.gkerlove.controller;

import com.gker.gkerlove.bean.Post;
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
        postService.addPost(user, postDto);
        return R.ok().message("发布成功");
    }

    @Operation(description = "获取当前登录用户发布的动态")
    @GetMapping("my")
    public R getCurrentUserPostList(@CurrentUser User user, int pageNumber) {
        return R.ok().data("postList", postService.retrieve(pageNumber, 5, user.getId()));
    }
}