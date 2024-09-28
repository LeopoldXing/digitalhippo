package com.leopoldhsing.digitalhippo.model.dto;

import com.leopoldhsing.digitalhippo.model.enumeration.NotificationType;

import java.util.Objects;

public class SnsMessageDto {

    private NotificationType type;

    private String email;

    private String verificationToken;

    public SnsMessageDto() {
    }

    public SnsMessageDto(NotificationType type, String email, String verificationToken) {
        this.type = type;
        this.email = email;
        this.verificationToken = verificationToken;
    }

    @Override
    public String toString() {
        return "NotificationDto{" +
                "type=" + type +
                ", email='" + email + '\'' +
                ", verificationToken='" + verificationToken + '\'' +
                '}';
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SnsMessageDto that = (SnsMessageDto) o;
        return type == that.type && Objects.equals(email, that.email) && Objects.equals(verificationToken, that.verificationToken);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(type);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(verificationToken);
        return result;
    }
}
