package com.gker.gkerlove.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "消息")
public class Message {
    @Schema(description = "消息id")
    String _id;
    @Schema(description = "创建时间")
    LocalDateTime timestamp;
    @Schema(description = "消息类型，text | image | disappearing")
    String type;
    @Schema(description = "发送者Id")
    String senderId;
    @Schema(description = "接收者Id")
    String recipientId;
    @Schema(description = "内容")
    String content;
    @Schema(description = "闪图是否查看")
    Boolean viewed;

}