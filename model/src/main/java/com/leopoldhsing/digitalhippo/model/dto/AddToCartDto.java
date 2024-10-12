package com.leopoldhsing.digitalhippo.model.dto;

import com.leopoldhsing.digitalhippo.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartDto {
    private User user;
    private List<Long> productIdList;
}
