package com.gker.gkerlove.controller;

import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.dto.SessionDto;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "聊天相关接口")
@RequestMapping("/message")
public class MessageController {
    @Resource
    MessageService messageService;

    @Operation(description = "获取用户聊天会话列表")
    @GetMapping
    public R getMessages(@CurrentUser User user) {
        List<SessionDto> sessionDtoList = messageService.retrieveMessages(user.getId());
        return R.ok().data("sessions", sessionDtoList);
    }
}