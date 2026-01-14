package com.company.ordersystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultDTO<T> {
    private String code = "99";//generic hatada 99 olsun
    private String message;
    private T data;
}
