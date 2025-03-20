package com.leopoldhsing.digitalhippo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationVo {

    private String payloadId;
    private String email;
    private String password;
    private String role;
    private List<Long> productIdList;
}
