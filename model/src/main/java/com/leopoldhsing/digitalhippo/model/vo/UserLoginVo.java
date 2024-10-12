package com.leopoldhsing.digitalhippo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVo {
    private String email;
    private String password;
    private List<Long> productIdList;
}
