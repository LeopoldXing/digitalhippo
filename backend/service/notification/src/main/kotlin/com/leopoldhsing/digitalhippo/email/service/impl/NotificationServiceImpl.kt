package com.leopoldhsing.digitalhippo.email.service.impl

import com.leopoldhsing.digitalhippo.email.config.EmailProperties
import com.leopoldhsing.digitalhippo.email.service.NotificationService
import com.leopoldhsing.digitalhippo.model.entity.Product
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class NotificationServiceImpl @Autowired constructor(
    private val javaMailSender: JavaMailSender,
    private val emailProperties: EmailProperties,
    private val resourceLoader: ResourceLoader // Used for loading resource files
) : NotificationService {

    companion object {
        private val log = getLogger(NotificationService::class.java)
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
        try {
            // 1. Create email message
            val message = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            // Set recipient, sender, and subject
            helper.setTo(email)
            helper.setFrom("no-reply@digitalhippo.com")
            helper.setSubject("DigitalHippo Receipt for order $orderPayloadId")

            // 2. Load email template
            val templateResource = resourceLoader.getResource("classpath:receipt_email_template.html")
            var htmlContent = templateResource.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }

            // 3. Replace global placeholders
            val dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")
            val invoiceDate = LocalDate.now().format(dateFormat)
            val total = products.fold(BigDecimal.ZERO) { acc, product -> acc.add(product.price) }.add(BigDecimal.ONE) // Transaction fee is 1

            htmlContent = htmlContent
                .replace("{{EMAIL}}", email)
                .replace("{{INVOICE_DATE}}", invoiceDate)
                .replace("{{ORDER_ID}}", orderPayloadId)
                .replace("{{TRANSACTION_FEE}}", formatPrice(BigDecimal.ONE))
                .replace("{{TOTAL_PRICE}}", formatPrice(total))

            // 4. Generate HTML section for product list
            val productsHtml = StringBuilder()

            for (product in products) {
                val productTemplate = """
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <!-- Product image -->
                                <td width="64" style="padding:10px;">
                                    <img src="${product.productImages?.firstOrNull()?.url ?: ""}" width="64" height="64" alt="Product Image" style="border-radius:14px; border:1px solid rgba(128,128,128,0.2);">
                                </td>
                                <!-- Product information -->
                                <td style="padding-left:22px;">
                                    <p style="font-size:12px; font-weight:600; margin:0;">${product.name}</p>
                                    <p style="font-size:12px; color:#666666; margin:0;">${product.description ?: ""}</p>
                                    <a href="${emailProperties.frontendEndpoint}/thank-you?orderId=$orderPayloadId" style="font-size:12px; color:#0070c9; text-decoration:none;">Download Asset</a>
                                </td>
                                <!-- Product price -->
                                <td align="right" style="padding-right:20px;">
                                    <p style="font-size:12px; font-weight:600; margin:0;">${formatPrice(product.price)}</p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                """.trimIndent()

                productsHtml.append(productTemplate)
            }

            // 5. Replace product list placeholder
            htmlContent = htmlContent.replace("{{PRODUCTS_SECTION}}", productsHtml.toString())

            // 6. Set email content
            helper.setText(htmlContent, true)

            // 7. Send email
            javaMailSender.send(message)
            log.info("Receipt email sent to: {}", email)
        } catch (e: Exception) {
            log.error("Failed to send receipt email to: {}", email, e)
        }
    }

    // Helper function: Format price
    private fun formatPrice(price: BigDecimal): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.CANADA)
        return formatter.format(price)
    }
}