package com.smokinggunstudio.vezerfonal.network.helpers

import com.smokinggunstudio.vezerfonal.data.NotificationData
import com.smokinggunstudio.vezerfonal.enums.NotificationType
import org.jetbrains.compose.resources.StringResource
import vezerfonal.composeapp.generated.resources.*

val NotificationData.pushNotifTextRes: StringResource
    get() = when(notifType) {
        NotificationType.Message -> Res.string.sent_a_message
        NotificationType.Nudge -> Res.string.check_out_message
        NotificationType.Reaction ->
            if (!data["reaction"].isNullOrEmpty()) Res.string.reacted_with
            else Res.string.has_marked_as_read
    }