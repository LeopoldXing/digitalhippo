package com.leopoldhsing.digitalhippo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseVo {
    private String accessToken;
    private List<ProductVo> productList;
}
