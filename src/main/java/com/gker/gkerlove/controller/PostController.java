package com.gker.gkerlove.controller;

import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.dto.req.AddPostReq;
import com.gker.gkerlove.bean.dto.req.CommentReq;
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
    public R addPost(@CurrentUser User user, @RequestBody AddPostReq addPostReq) {
        PostDto postDto = postService.addPost(user, addPostReq);
        return R.ok().message("发布成功").data("post", postDto);
    }

    @Operation(description = "检索动态")
    @GetMapping
    public R retrieve(@RequestParam(value = "page") Integer page) {
        return R.ok().data("posts", postService.retrieve(page, 10, null));
    }

    @Operation(description = "删除动态")
    @DeleteMapping("{id}")
    public R deletePost(@CurrentUser User user, @PathVariable("id") String id) {
        postService.deletePost(user, id);
        return R.ok().message("删除成功");
    }

    @Operation(description = "获取用户发布的动态")
    @GetMapping("user/{id}")
    public R getUserPostList(@PathVariable("id") String id) {
        return R.ok().data("posts", postService.retrieve(null, null, id));
    }

    @Operation(description = "根据动态id获取动态信息")
    @GetMapping("{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok().data("post", postService.getById(id));
    }

    @Operation(description = "评论动态")
    @PostMapping("comment/{id}")
    public R comment(@CurrentUser User user, @PathVariable("id") String id, @RequestBody CommentReq commentReq) {
        PostDto.Comment comment = postService.commentOnPost(user, commentReq, id);
        return R.ok().message("评论成功").data("comment", comment);
    }
}