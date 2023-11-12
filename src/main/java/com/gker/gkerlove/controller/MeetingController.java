package com.gker.gkerlove.controller;

import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "遇见相关接口")
@RequestMapping("/meeting")
public class MeetingController {
    @Resource
    MeetingService meetingService;

    @Operation(description = "发送喜欢请求")
    @PostMapping("addlove")
    public R addlove(@RequestParam String fromusername,@RequestParam String tousername) {
        String s = meetingService.MeetingAddlover(fromusername, tousername);
        JSONObject parse =(JSONObject) JSONObject.parse(s);
        String r = parse.getString("ActionStatus");
        if(!r.equals("OK")){
            throw new GKerLoveException("发送喜欢请求失败,错误信息:"+parse.getString("ErrorInfo"));
        }
        return R.ok().message("已发送喜欢请求");
    }
}
