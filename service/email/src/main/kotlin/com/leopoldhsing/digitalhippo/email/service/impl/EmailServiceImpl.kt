package com.leopoldhsing.digitalhippo.email.service.impl

import com.leopoldhsing.digitalhippo.email.config.EmailProperties
import com.leopoldhsing.digitalhippo.email.service.EmailService
import com.leopoldhsing.digitalhippo.model.entity.Product
import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl @Autowired constructor(
    private val javaMailSender: JavaMailSender,
    private val emailProperties: EmailProperties
) : EmailService {

    companion object {
        private val log = getLogger(EmailServiceImpl::class.java)
    }

    override fun sendVerificationEmail(email: String, verificationToken: String): Boolean {
        // 1. construct email message
        val message = javaMailSender.createMimeMessage()
        // recipient
        message.setRecipients(MimeMessage.RecipientType.TO, email)
        // subject
        message.subject = "DigitalHippo Account Verification"
        // content
        val htmlContent = "<a href='${emailProperties.frontendEndpoint}/verify-email?token=${verificationToken}'>verify account</a>"
        message.setContent(htmlContent, "text/html; charset=utf-8")

        // 2. send email
        javaMailSender.send(message)

        log.info("verification email sent: {}", message)
        return true;
    }

    override fun sendReceiptEmail(
        email: String,
        orderPayloadId: String,
        products: List<Product>
    ) {
        // 1. construct email message
        val message = javaMailSender.createMimeMessage()
        // recipient
        message.setRecipients(MimeMessage.RecipientType.TO, email)
        // subject
        message.subject = "DigitalHippo Receipt for order $orderPayloadId"
        // content
        val htmlContent = "111111111"
        message.setContent(htmlContent, "text/html; charset=utf-8")

        // 2. send email
        javaMailSender.send(message)

        log.info("receipt email sent: {}", message)
    }
}