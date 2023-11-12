package com.gker.gkerlove.bean.response;

import com.gker.gkerlove.bean.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息Dto")
public class UserDto {
    @Schema(description = "用户名")
    String username;
    @Schema(description = "邮箱")
    String email;
    @Schema(description = "用户信息")
    User.UserInfo info;
}