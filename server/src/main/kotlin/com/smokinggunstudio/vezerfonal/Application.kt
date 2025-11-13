package com.smokinggunstudio.vezerfonal

import com.smokinggunstudio.vezerfonal.database.configureDatabase
import com.smokinggunstudio.vezerfonal.helpers.ImageService
import com.smokinggunstudio.vezerfonal.router.configureRouting
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main() {
    embeddedServer(
        factory = Netty,
        environment = applicationEnvironment {
            config = HoconApplicationConfig(ConfigFactory.load())
        },
        configure = {
            configureBootstrap = {
                this.childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel?) {
                        val pipeline = ch?.pipeline()
                        val aggregatorName = "httpAggregator"
                        val newAggregator = HttpObjectAggregator(Int.MAX_VALUE)
                        if (pipeline?.get(aggregatorName) != null)
                            pipeline.replace(aggregatorName, aggregatorName, newAggregator)
                        else pipeline?.addLast(aggregatorName, newAggregator)
                    }
                })
            }
        }
    ){ module() }.start(wait = true)
}

fun Application.module() = runBlocking {
    val url: String? = environment.config.propertyOrNull("ktor.database.url")?.getString()
    val username: String? = environment.config.propertyOrNull("ktor.database.username")?.getString()
    val password: String? = environment.config.propertyOrNull("ktor.database.password")?.getString()
    val pfpDirectories = environment.config.propertyOrNull("ktor.application.directories.pfps")?.getString()
    val context = Dispatchers.IO
    val imageService = if (pfpDirectories != null) ImageService(pfpDirectories)
    else throw NoSuchFieldException("Unable to get path to pictures directory: No such environment variable.")
    
    if (url != null && username != null && password != null)
        async(context) { configureDatabase(url, username, password, context) }.await()
    configureRouting(imageService, context)
}