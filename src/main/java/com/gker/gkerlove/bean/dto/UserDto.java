package com.gker.gkerlove.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Schema(description = "喜欢")
    private Integer likes = 0;

    @Schema(description = "人气")
    private Integer likedBy = 0;

    @Schema(description = "喜欢我的用户Id列表")
    private List<String> likedByUserIdList = new ArrayList<>();

    @Schema(description = "喜欢的用户Id列表")
    private List<String> likeUserIdList = new ArrayList<>();

    @Schema(description = "是否在线")
    private boolean online;

    @Schema(description = "最后上线时间")
    private LocalDateTime lastOnline;
}