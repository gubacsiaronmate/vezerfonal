package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
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
    
    private val sendSemaphore = Semaphore(32)
    
    suspend fun broadcast(message: Message, db: Database) = coroutineScope {
        val recipients: List<User> = message.user?.let { listOf(it) }
            ?: message.group!!.members.map { it.user }
        
        recipients.map { user ->
            async {
                sendSemaphore.withPermit {
                    val msg = message.fillMissingInformation(db, user.id!!, user.isAnyAdmin == true)
                    userChannels[user.id]?.forEach { channel ->
                        channel.trySend(msg)
                    }
                }
            }
        }.awaitAll()
    }
}