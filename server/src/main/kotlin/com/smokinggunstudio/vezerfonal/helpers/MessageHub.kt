package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.models.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

object MessageHub {
    private val userChannels = ConcurrentHashMap<Int, MutableList<Channel<MessageData>>>()
    
    fun subscribe(userId: Int): Channel<MessageData> =
        Channel<MessageData>(Channel.UNLIMITED).let { channel ->
            userChannels.compute(userId) { _, list -> (list ?: mutableListOf()).apply { add(channel) } }
            channel
        }
    
    fun unsubscribe(userId: Int, channel: Channel<MessageData>) {
        userChannels[userId]?.remove(channel)
        channel.close()
    }
    
    suspend fun broadcast(message: Message, context: CoroutineContext) = withContext(context) {
        val recipients: List<User> = message.user?.let { listOf(it) }
            ?: message.group!!.members.map { it.user }
        
        recipients.forEach { user ->
            userChannels[user.id]?.forEach { it.trySend(message.toDTO()) }
        }
    }
}