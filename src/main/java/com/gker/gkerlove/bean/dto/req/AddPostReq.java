package com.gker.gkerlove.bean.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "发布动态请求")
public class AddPostReq {
    @Schema(description = "动态内容")
    String content;

    @Schema(description = "动态图片列表")
    List<String> imageList;

    @Schema(description = "是否匿名")
    Boolean anonymous;
}