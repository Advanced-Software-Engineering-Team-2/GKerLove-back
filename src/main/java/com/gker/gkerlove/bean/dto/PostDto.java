package com.gker.gkerlove.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "动态Dto")
public class PostDto {
    @Schema(description = "动态ID")
    String id;

    @Schema(description = "动态内容")
    String content;

    @Schema(description = "动态图片列表")
    List<String> imageList;
}