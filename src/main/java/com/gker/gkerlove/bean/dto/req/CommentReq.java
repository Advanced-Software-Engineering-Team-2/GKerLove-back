package com.gker.gkerlove.bean.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "评论请求")
public class CommentReq {
    @Schema(description = "评论内容")
    String content;
}