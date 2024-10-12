package com.leopoldhsing.digitalhippo.model.dto;

import com.leopoldhsing.digitalhippo.model.enumeration.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnsMessageDto {

    private NotificationType type;

    private String email;

    private String verificationToken;
}
