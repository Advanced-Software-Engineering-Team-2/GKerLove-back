package com.gker.gkerlove.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "聊天会话")
@Document("sessions")
public class Session {
    @Schema(description = "会话id")
    String id;
    @Schema(description = "发起人id")
    String initiatorId;
    @Schema(description = "接收人id")
    String recipientId;
    @Schema(description = "会话最后更新时间，用于会话排序")
    LocalDateTime lastUpdated;
    @Schema(description = "发起人最后阅读时间")
    LocalDateTime initiatorLastRead;
    @Schema(description = "接收人最后阅读时间")
    LocalDateTime recipientLastRead;
    @Schema(description = "消息")
    List<Message> messages = new ArrayList<>();
}