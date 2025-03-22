package com.leopoldhsing.digitalhippo.notification.service

import com.leopoldhsing.digitalhippo.model.entity.Product

interface NotificationService {

    fun sendVerificationEmail(email: String, verificationToken: String): Boolean
    fun sendReceiptEmail(email: String, orderPayloadId: String, products: List<Product>)

}