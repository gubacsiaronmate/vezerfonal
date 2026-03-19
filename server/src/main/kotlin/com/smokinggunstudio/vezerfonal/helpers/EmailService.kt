package com.smokinggunstudio.vezerfonal.helpers

import com.resend.Resend
import com.resend.services.emails.model.CreateEmailOptions

object EmailService {
    private lateinit var client: Resend
    private lateinit var fromAddress: String
    private val renderer = ThymeleafRenderer()

    fun initialize(apiKey: String, from: String) {
        client = Resend(apiKey)
        fromAddress = from
    }

    fun sendVerificationCode(toEmail: String, code: Int) {
        val html = renderer.render("verification_code", mapOf("code" to code.toString()))
        val options = CreateEmailOptions.builder()
            .from("Vezérfonal <$fromAddress>")
            .to(toEmail)
            .subject("Vezérfonal – Ellenőrző kód")
            .html(html)
            .build()
        client.emails().send(options)
    }
}
