package com.avinash.danumalk.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResultResponse<T> {
    private String status;
    private T data;
}
