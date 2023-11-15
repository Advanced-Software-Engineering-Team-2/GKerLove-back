package com.gker.gkerlove.bean.dto;

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
    @Schema(description = "人气")
    Integer likedBy;
    @Schema(description = "喜欢")
    Integer likes;

}