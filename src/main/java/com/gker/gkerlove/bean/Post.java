package com.gker.gkerlove.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "动态")
@Document("posts")
public class Post {
    @Schema(description = "动态ID")
    String id;

    @Schema(description = "发布动态的用户ID")
    String userId;

    @Schema(description = "动态内容")
    String content;

    @Schema(description = "动态图片列表")
    List<String> imageList;

    @Schema(description = "发布时间")
    LocalDateTime time;

    @Schema(description = "评论列表")
    List<Comment> commentList = new ArrayList<>();

    @Data
    @Schema(description = "评论")
    public static class Comment {
        @Schema(description = "评论ID")
        String id;

        @Schema(description = "评论用户ID")
        String userId;

        @Schema(description = "评论内容")
        String content;

        @Schema(description = "评论时间")
        LocalDateTime time;
    }
}