package com.smokinggunstudio.vezerfonal.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

actual fun provideEngine(): HttpClientEngine = CIO.create()