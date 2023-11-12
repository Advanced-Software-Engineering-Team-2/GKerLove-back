package com.gker.gkerlove.bean.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {
    long total;
    List<T> content;
}