package com.leopoldhsing.digitalhippo.model.dto;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.leopoldhsing.digitalhippo.model.enumeration.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessageDto {
    private NotificationType type;
    private String email;
    private String verificationToken;
    private String orderPayloadId;
    private List<Product> products;
}
