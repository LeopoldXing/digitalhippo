package com.leopoldhsing.digitalhippo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SnsNotificationDto {
    @JsonProperty("Type")
    private String type;

    @JsonProperty("MessageId")
    private String messageId;

    @JsonProperty("TopicArn")
    private String topicArn;

    @JsonProperty("Subject")
    private String subject;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("SignatureVersion")
    private String signatureVersion;

    @JsonProperty("Signature")
    private String signature;

    @JsonProperty("SigningCertURL")
    private String signingCertURL;

    @JsonProperty("UnsubscribeURL")
    private String unsubscribeURL;

    public SnsNotificationDto() {
    }

    public SnsNotificationDto(String type, String messageId, String topicArn, String subject, String message, String timestamp, String signatureVersion, String signature, String signingCertURL, String unsubscribeURL) {
        this.type = type;
        this.messageId = messageId;
        this.topicArn = topicArn;
        this.subject = subject;
        this.message = message;
        this.timestamp = timestamp;
        this.signatureVersion = signatureVersion;
        this.signature = signature;
        this.signingCertURL = signingCertURL;
        this.unsubscribeURL = unsubscribeURL;
    }

    @Override
    public String toString() {
        return "SnsNotificationDto{" +
                "type='" + type + '\'' +
                ", messageId='" + messageId + '\'' +
                ", topicArn='" + topicArn + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", signatureVersion='" + signatureVersion + '\'' +
                ", signature='" + signature + '\'' +
                ", signingCertURL='" + signingCertURL + '\'' +
                ", unsubscribeURL='" + unsubscribeURL + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignatureVersion() {
        return signatureVersion;
    }

    public void setSignatureVersion(String signatureVersion) {
        this.signatureVersion = signatureVersion;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSigningCertURL() {
        return signingCertURL;
    }

    public void setSigningCertURL(String signingCertURL) {
        this.signingCertURL = signingCertURL;
    }

    public String getUnsubscribeURL() {
        return unsubscribeURL;
    }

    public void setUnsubscribeURL(String unsubscribeURL) {
        this.unsubscribeURL = unsubscribeURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SnsNotificationDto that = (SnsNotificationDto) o;
        return Objects.equals(type, that.type) && Objects.equals(messageId, that.messageId) && Objects.equals(topicArn, that.topicArn) && Objects.equals(subject, that.subject) && Objects.equals(message, that.message) && Objects.equals(timestamp, that.timestamp) && Objects.equals(signatureVersion, that.signatureVersion) && Objects.equals(signature, that.signature) && Objects.equals(signingCertURL, that.signingCertURL) && Objects.equals(unsubscribeURL, that.unsubscribeURL);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(type);
        result = 31 * result + Objects.hashCode(messageId);
        result = 31 * result + Objects.hashCode(topicArn);
        result = 31 * result + Objects.hashCode(subject);
        result = 31 * result + Objects.hashCode(message);
        result = 31 * result + Objects.hashCode(timestamp);
        result = 31 * result + Objects.hashCode(signatureVersion);
        result = 31 * result + Objects.hashCode(signature);
        result = 31 * result + Objects.hashCode(signingCertURL);
        result = 31 * result + Objects.hashCode(unsubscribeURL);
        return result;
    }
}
