package com.gker.gkerlove.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

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
}