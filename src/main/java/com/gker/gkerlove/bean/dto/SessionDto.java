package com.gker.gkerlove.bean.dto;

import com.gker.gkerlove.bean.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "会话Dto")
public class SessionDto {
    @Schema(description = "会话id")
    String id;
    @Schema(description = "对方")
    UserDto peer;
    @Schema(description = "最后阅读时间")
    LocalDateTime lastRead;
    @Schema(description = "对方最后阅读时间")
    LocalDateTime peerLastRead;
    @Schema(description = "消息")
    List<Message> messages = new ArrayList<>();
}