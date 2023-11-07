package com.gker.gkerlove.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户")
@Document("users")
public class User extends UserDTO {
    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "是否填写aboutme信息")
    private Boolean hasaboutme;
}