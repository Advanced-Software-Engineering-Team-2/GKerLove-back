package com.gker.gkerlove.bean.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Schema(description = "通用返回结果")
public class R {
    @Schema(description = "结果（成功/失败）")
    private ResultCode code;
    @Schema(description = "信息")
    private String message;

    @Schema(description = "数据")
    private Map<String, Object> data = new HashMap<>();

    private R() {
    }

    public static R ok() {
        R result = new R();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("成功");
        return result;
    }

    public static R error() {
        R result = new R();
        result.setCode(ResultCode.ERROR);
        result.setMessage("失败");
        return result;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(ResultCode code) {
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

    public enum ResultCode {
        SUCCESS,
        ERROR,
        UNAUTHORIZED,
        UNAUTHENTICATED

    }
}
