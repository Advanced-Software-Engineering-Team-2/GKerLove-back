package com.gker.gkerlove.controller;

import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.dto.UserDto;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "遇见相关接口")
@RequestMapping("/meeting")
public class MeetingController {
    @Resource
    MeetingService meetingService;

    @Operation(description = "获取遇见列表")
    @GetMapping("getlist")
    public R getlist( @RequestParam String fromusername,
                      @RequestParam String gender,
                      @RequestParam Integer min_age,
                      @RequestParam Integer max_age,
                      @RequestParam String city,
                      @RequestParam String institute) {
        List<UserDto> meetinglist = new ArrayList<>();
        List<User> userlists = meetingService.MeetingGetlist(fromusername, gender, min_age, max_age, city, institute);
        for (User user : userlists) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            meetinglist.add(userDto);
        }
        return R.ok().data("meetinglist", meetinglist);
    }



    @Operation(description = "发送喜欢请求")
    @PostMapping("addlove")
    public R addlove(@RequestParam String fromusername,@RequestParam String tousername) {
        String s = meetingService.MeetingAddlover(fromusername, tousername);
        meetingService.MeetingNotlove(fromusername, tousername);
        JSONObject parse =(JSONObject) JSONObject.parse(s);
        String r = parse.getString("ActionStatus");
        if(!r.equals("OK")){
            throw new GKerLoveException("发送喜欢请求失败,错误信息:"+parse.getString("ErrorInfo"));
        }
        return R.ok().message("已发送喜欢请求");
    }

    @Operation(description = "发送不喜欢请求")
    @PostMapping("notlove")
    public R notlove(@RequestParam String fromusername,@RequestParam String tousername) {
        meetingService.MeetingNotlove(fromusername, tousername);
        return R.ok().message("已不再推荐该用户");
    }

}
