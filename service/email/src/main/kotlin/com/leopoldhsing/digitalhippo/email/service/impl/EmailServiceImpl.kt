package com.leopoldhsing.digitalhippo.email.service.impl

import com.leopoldhsing.digitalhippo.email.config.EmailProperties
import com.leopoldhsing.digitalhippo.email.service.EmailService
import com.leopoldhsing.digitalhippo.model.entity.Product
import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl @Autowired constructor(
    private val javaMailSender: JavaMailSender,
    private val emailProperties: EmailProperties,
    private val resourceLoader: ResourceLoader // Used for loading resource files
) : EmailService {

    companion object {
        private val log = getLogger(EmailServiceImpl::class.java)
    }

    override fun sendVerificationEmail(email: String, verificationToken: String): Boolean {
        try {
            // 1. Build the email
            val message = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8") // The second parameter is true, meaning multipart support

            // Set recipient
            helper.setTo(email)
            // Set sender
            helper.setFrom("no-reply@digitalhippo.com")
            // Set subject
            helper.setSubject("DigitalHippo Account Verification")

            // 2. Load email template
            val templateResource = resourceLoader.getResource("classpath:verification_email_template.html")
            val template = templateResource.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }

            // 3. Replace placeholders
            val actionLabel = "verify your email"
            val buttonText = "Verify Account"
            val href = "${emailProperties.frontendEndpoint}/verify-email?token=$verificationToken"
            var htmlContent = template
                .replace("{{ACTION_LABEL}}", actionLabel)
                .replace("{{BUTTON_TEXT}}", buttonText)
                .replace("{{BUTTON_HREF}}", href)

            // 4. Set email content
            helper.setText(htmlContent, true) // The second parameter is true, meaning the content is HTML

            // 5. Send email
            javaMailSender.send(message)

            log.info("Verification email sent to: {}", email)
            return true
        } catch (e: Exception) {
            log.error("Failed to send verification email to: {}", email, e)
            return false
        }
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