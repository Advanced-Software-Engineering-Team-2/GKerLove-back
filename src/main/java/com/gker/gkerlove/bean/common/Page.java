package com.gker.gkerlove.bean.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {
    @Schema(description = "总记录数")
    long total;
    @Schema(description = "当前页数据")
    List<T> content;
}