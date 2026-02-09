package com.smokinggunstudio.vezerfonal.ui.helpers

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging

actual suspend fun getPushToken(): String? = Firebase.messaging.getToken()