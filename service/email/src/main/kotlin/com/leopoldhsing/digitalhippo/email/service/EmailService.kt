package com.leopoldhsing.digitalhippo.email.service

interface EmailService {

    fun sendVerificationEmail(email: String, verificationToken: String): Boolean

}