package com.gker.gkerlove.controller;


import com.aliyuncs.exceptions.ClientException;
import com.gker.gkerlove.bean.R;
import com.gker.gkerlove.bean.UserDTO;
import com.gker.gkerlove.interceptor.Login;
import com.gker.gkerlove.resolver.CurrentUser;
import com.gker.gkerlove.service.OSSService;
import com.gker.gkerlove.util.OssClientUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/oss")
@Tag(name = "OSS相关接口")
@Login
public class OSSController {
    @Resource
    OSSService ossService;


    @PostMapping("/upload")
    public String fileupload(@RequestParam MultipartFile file) throws IOException {
        if (file == null || file.getSize() <= 0) {
            throw new IOException("file不能为空");
        }
        OssClientUtil ossClient = new OssClientUtil();
        //将文件上传
        String name = ossClient.uploadImg2Oss(file);
        //获取文件的URl地址  以便前台  显示
        String imgUrl = ossClient.getImgUrl(name);
        return imgUrl;

    }

    @Operation(description = "获取OSS临时操作凭证")
    @GetMapping("token")
    public R getToken(@CurrentUser UserDTO user) throws ClientException {
        return R.ok().data("stsResponse", ossService.assumeRole(user.getUsername()));
    }


}
