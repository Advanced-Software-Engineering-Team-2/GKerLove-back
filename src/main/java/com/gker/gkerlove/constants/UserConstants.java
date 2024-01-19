package com.gker.gkerlove.constants;

import com.gker.gkerlove.bean.User;

public class UserConstants {
    public static final User anonymous = new User();

    static {
        anonymous.setId("Anonymous");
        anonymous.setUsername("Anonymous");
        anonymous.setAvatar("https://gker-love.oss-cn-beijing.aliyuncs.com/Anonymous.png");
    }
}