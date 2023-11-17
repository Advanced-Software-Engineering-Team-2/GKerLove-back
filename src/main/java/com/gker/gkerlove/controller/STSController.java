package com.gker.gkerlove.controller;


import com.aliyuncs.exceptions.ClientException;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.interceptor.Login;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.STSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sts")
@Tag(name = "OSS相关接口")
@Login
public class STSController {
    @Resource
    STSService STSService;

    @Operation(description = "获取OSS临时操作凭证")
    @GetMapping("token")
    public R getToken(@CurrentUser User user) throws ClientException {
        return R.ok().data("stsResponse", STSService.assumeRole(user.getId()));
    }


}
