package com.smokinggunstudio.vezerfonal.network.client

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

actual fun provideEngine(): HttpClientEngine = CIO.create()