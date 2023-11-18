package com.gker.gkerlove.bean.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "注册请求")
public class RegisterReq {
    @Schema(description = "用户名")
    String username;
    @Schema(description = "密码")
    String password;
    @Schema(description = "邮箱")
    String email;
    @Schema(description = "邮箱验证码")
    String code;
    @Schema(description = "图片验证码")
    String captcha;
}