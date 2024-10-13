package com.leopoldhsing.digitalhippo.model.vo.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCheckoutSessionVo {
    private String payloadOrderId;
    private List<Long> productIdList;
}
