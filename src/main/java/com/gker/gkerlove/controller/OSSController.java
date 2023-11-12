package com.gker.gkerlove.controller;


import com.aliyuncs.exceptions.ClientException;
import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.interceptor.Login;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.OSSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/OSS")
@Tag(name = "OSS相关接口")
@Login
public class OSSController {
    @Resource
    OSSService ossService;

    @Operation(description = "获取OSS临时操作凭证")
    @GetMapping("token")
    public R getToken(@CurrentUser User user) throws ClientException {
        return R.ok().data("stsResponse", ossService.assumeRole(user.getUsername()));
    }


}
