package com.smokinggunstudio.vezerfonal.network

import android.R.attr.text
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class FirebasePushService : FirebaseMessagingService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
    
    override fun onMessageReceived(message: RemoteMessage) {
        serviceScope.launch {
            val data = runCatching {
                message.data["data"]?.toDTO<NotificationData>()
            }.onFailure { e ->
                log(10) { "Failed to parse notification: ${e.message}" }
                e.printStackTrace()
            }.getOrNull() ?: return@launch
            
            val title = data.title
            val body = data.convertToReadableText()
            
            showNotification(title, body)
        }
    }
    
    private suspend fun NotificationData.convertToReadableText(): String = coroutineScope {
        val strRes = org.jetbrains.compose.resources.getString(pushNotifTextRes)
        when(notifType) {
            NotificationType.Message -> "${data["sender"].orEmpty()} $strRes"
            NotificationType.Nudge -> "$strRes ${data["message"].orEmpty()}"
            NotificationType.Reaction -> {
                val (middleTxt, endTxt) = strRes.split('|', limit = 2)
                
                "$middleTxt ${data["extra"] ?: data["reaction"]} $endTxt"
            }
        }
    }
    
    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "high_priority_messages"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Messages", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        
        val notification = NotificationCompat
            .Builder(this@FirebasePushService, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.small_logo)
            .setAutoCancel(true)
            .build()
        
        val notifId = (title + body + System.currentTimeMillis().toString()).hashCode()
        
        notificationManager.notify(notifId, notification)
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