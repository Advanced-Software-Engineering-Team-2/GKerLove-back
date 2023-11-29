package com.gker.gkerlove.bean.dto;

import com.gker.gkerlove.bean.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "会话Dto")
public class SessionDto {
    @Schema(description = "会话id")
    String id;
    @Schema(description = "发起人")
    UserDto initiator;
    @Schema(description = "接收人")
    UserDto recipient;
    @Schema(description = "会话最后更新时间，用于会话排序")
    String lastUpdated;
    @Schema(description = "发起人最后阅读时间")
    String initiatorLastRead;
    @Schema(description = "接收人最后阅读时间")
    String recipientLastRead;
    @Schema(description = "消息")
    List<Message> messages = new ArrayList<>();
}