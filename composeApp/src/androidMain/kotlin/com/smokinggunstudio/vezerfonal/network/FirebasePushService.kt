package com.smokinggunstudio.vezerfonal.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smokinggunstudio.vezerfonal.R
import com.smokinggunstudio.vezerfonal.data.NotificationData
import com.smokinggunstudio.vezerfonal.enums.NotificationType
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.network.api.registerPushToken
import com.smokinggunstudio.vezerfonal.network.client.createHttpClient
import com.smokinggunstudio.vezerfonal.network.helpers.Platform
import com.smokinggunstudio.vezerfonal.network.helpers.pushNotifTextRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FirebasePushService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: "New message"
        val body = message.notification?.body ?: ""
        
        showNotification(title, body)
    }
    
    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "high_priority_messages"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Messages", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        
        CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        ).launch {
            val text = async {
                return@async with(body.toDTO<NotificationData>()) {
                    val strRes = org.jetbrains.compose.resources.getString(pushNotifTextRes)
                    return@with when(this.notifType) {
                        NotificationType.Message -> "${this.data["sender"]} $strRes"
                        NotificationType.Reaction -> {
                            val (middleTxt, endTxt) = strRes.split("|")
                            val extra = this.data["extra"]
                            
                            "$middleTxt $extra $endTxt"
                        }
                        NotificationType.Nudge -> "$strRes ${this.data["message"]}"
                    }
                }
            }.await()
            
            
            val notification = NotificationCompat
                .Builder(this@FirebasePushService, channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.small_logo)
                .setAutoCancel(true)
                .build()
            
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        
        scope.launch {
            try {
                val tokenStorage = TokenStorage()
                val tokens = tokenStorage.getTokens()
                
                if (tokens != null) {
                    val client = createHttpClient()
                    registerPushToken(
                        accessToken = tokens.accessToken,
                        client = client,
                        fcmToken = token,
                        platform = Platform.type.name.lowercase()
                    )
                }
            } catch (e: Exception) {
                val sTrace = e.stackTrace.joinToString("\n") { it.toString() }
                log(15) { "${e.message}\n${sTrace}" }
            }
        }
    }
}