package com.gker.gkerlove.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "动态Dto")
public class PostDto {
    @Schema(description = "动态ID")
    String id;

    @Schema(description = "发布用户")
    UserDto user;

    @Schema(description = "动态内容")
    String content;

    @Schema(description = "动态图片列表")
    List<String> imageList;

    @Schema(description = "发布时间")
    LocalDateTime time;

    @Schema(description = "评论列表")
    List<Comment> commentList = new ArrayList<>();

    @Data
    @Schema(description = "评论Dto")
    public static class Comment {
        @Schema(description = "评论ID")
        String id;

        @Schema(description = "评论用户")
        UserDto user;

        @Schema(description = "评论内容")
        String content;

        @Schema(description = "评论时间")
        LocalDateTime time;
    }
}