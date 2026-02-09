package com.smokinggunstudio.vezerfonal.helpers

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import com.smokinggunstudio.vezerfonal.data.NotificationData
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
        data: NotificationData
    ) {
        if (tokens.isEmpty()) return
        
        val message = MulticastMessage.builder()
            .addAllTokens(tokens)
            .putAllData(mapOf("data" to data.toSerialized()))
            .build()
        
        FirebaseMessaging.getInstance().sendEachForMulticastAsync(message)
    }
}