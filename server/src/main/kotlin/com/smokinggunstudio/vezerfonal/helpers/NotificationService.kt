package com.smokinggunstudio.vezerfonal.helpers

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import java.io.FileInputStream

object NotificationService {
    fun initialize(serviceAccountPath: String) {
        val serviceAccount = FileInputStream(serviceAccountPath)
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()
        FirebaseApp.initializeApp(options)
    }
    
    fun sendPushNotification(
        tokens: List<String>,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) {
        if (tokens.isEmpty()) return
        
        val message = MulticastMessage.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()
            )
            .addAllTokens(tokens)
            .putAllData(data)
            .build()
        
        FirebaseMessaging.getInstance().sendEachForMulticastAsync(message)
    }
}