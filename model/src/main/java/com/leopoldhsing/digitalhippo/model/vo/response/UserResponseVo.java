package com.leopoldhsing.digitalhippo.model.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseVo {
    private String id;
    private String role;
    private String email;
    private Boolean _verified;
}
