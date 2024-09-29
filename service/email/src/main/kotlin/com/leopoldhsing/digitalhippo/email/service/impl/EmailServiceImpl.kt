package com.leopoldhsing.digitalhippo.email.service.impl

import com.leopoldhsing.digitalhippo.email.config.EmailProperties
import com.leopoldhsing.digitalhippo.email.service.EmailService
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl @Autowired constructor(
    private val javaMailSender: JavaMailSender,
    private val emailProperties: EmailProperties
) : EmailService {

    override fun sendVerificationEmail(email: String, verificationToken: String): Boolean {
        // 1. construct email message
        val message = javaMailSender.createMimeMessage()
        // recipient
        message.setRecipients(MimeMessage.RecipientType.TO, email)
        // subject
        message.subject = "DigitalHippo Account Verification"
        // content
        val htmlContent = "<a href='${emailProperties.frontendAddress}/verify-email?token=${verificationToken}'>verify account</a>"
        message.setContent(htmlContent, "text/html; charset=utf-8")

        // 2. send email
        javaMailSender.send(message)
        return true;
    }
}