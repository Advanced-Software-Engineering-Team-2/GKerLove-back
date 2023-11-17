package com.gker.gkerlove.bean.dto;

import com.gker.gkerlove.bean.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息Dto")
public class UserDto {
    @Schema(description = "用户id")
    String id;

    @Schema(description = "用户名")
    String username;

    @Schema(description = "邮箱")
    String email;

    @Schema(description = "用户头像")
    private String avatar = "default-avatar";

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "所在城市")
    private String city;

    @Schema(description = "培养单位")
    private String institute;

    @Schema(description = "自我介绍")
    private String introduction;

    @Schema(description = "人气")
    Integer likedBy;

    @Schema(description = "喜欢")
    Integer likes;
}