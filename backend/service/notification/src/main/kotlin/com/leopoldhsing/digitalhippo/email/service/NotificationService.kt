package com.leopoldhsing.digitalhippo.email.service

import com.leopoldhsing.digitalhippo.model.entity.Product

interface NotificationService {

    fun sendVerificationEmail(email: String, verificationToken: String): Boolean
    fun sendReceiptEmail(email: String, orderPayloadId: String, products: List<Product>)

}