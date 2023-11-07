package com.gker.gkerlove.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Schema(description = "用户详细介绍")
@Document("about")
public class Aboutme {
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "用户年龄")
    private String age;

    @Schema(description = "常驻城市")
    private String city;

    @Schema(description = "培养单位")
    private String institute;

    @Schema(description = "培养专业")
    private String major;

    @Schema(description = "自我介绍")
    private String introduction;

}
