package com.smokinggunstudio.vezerfonal.network.client

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual fun provideEngine(): HttpClientEngine = OkHttp.create()