package com.gker.gkerlove.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息，去掉User类中的密码，创建时间，是否禁用等信息")
public class UserDTO {
    @Schema(description = "用户id")
    private String id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "是否填写aboutme信息")
    private Boolean hasaboutme;
}