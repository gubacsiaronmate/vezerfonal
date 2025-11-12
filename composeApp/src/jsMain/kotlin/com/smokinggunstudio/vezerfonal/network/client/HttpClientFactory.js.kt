package com.smokinggunstudio.vezerfonal.network.client

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual fun provideEngine(): HttpClientEngine = Js.create()