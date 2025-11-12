package com.smokinggunstudio.vezerfonal.network.client

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual fun provideEngine(): HttpClientEngine = Darwin.create()