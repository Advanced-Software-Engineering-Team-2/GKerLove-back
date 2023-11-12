package com.gker.gkerlove.controller;

import com.gker.gkerlove.bean.common.R;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.service.CodeService;
import com.gker.gkerlove.util.CaptchaUtil;
import com.gker.gkerlove.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@Tag(name = "公共接口")
@RequestMapping("/common")
public class CommonController {
    @Resource
    CodeService codeService;

    @Resource
    MongoTemplate mongoTemplate;

    @Operation(description = "发送邮箱验证码")
    @GetMapping("code")

    public R registerSendCode(String email) {
        if (email.length() > 50) throw new GKerLoveException("邮箱长度不能大于50");
        codeService.sendCode(email);
        return R.ok().message("发送成功");
    }

    @Operation(description = "生成图片验证码")
    @GetMapping("captcha")
    public void generateCaptcha(HttpServletResponse response, HttpSession httpSession) throws IOException {
        String captcha = RandomUtil.getFourBitRandom();
        httpSession.setAttribute("captcha", captcha);
        response.setContentType("image/png");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream outputStream = response.getOutputStream();
        ImageIO.write(CaptchaUtil.generateImage(captcha), "png", outputStream);
        outputStream.flush();
        outputStream.close();
    }
}