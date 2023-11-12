package com.gker.gkerlove.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户")
@Document("users")
public class User {
    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "用户信息")
    private UserInfo info = new UserInfo();

    @Data
    @Schema(description = "用户信息")
    public static class UserInfo {
        @Schema(description = "用户头像")
        private String avatar = "default-avatar";

        @Schema(description = "性别")
        private String gender;

        @Schema(description = "性别")
        private Integer age;

        @Schema(description = "所在城市")
        private String city;

        @Schema(description = "培养单位")
        private String institute;

        @Schema(description = "自我介绍")
        private String introduction;
    }
}