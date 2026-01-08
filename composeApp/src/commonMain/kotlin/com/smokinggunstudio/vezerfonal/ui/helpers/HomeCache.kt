package com.smokinggunstudio.vezerfonal.ui.helpers

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.api.*
import io.ktor.client.*

object HomeCache {
    private var loaded = false

    var user: UserData? = null
    var groups: List<GroupData> = emptyList()
    var regCodes: List<RegCodeData> = emptyList()
    var guiao: List<GroupData> = emptyList()
    var userList: List<UserData> = emptyList()
    var tagList: List<TagData> = emptyList()

    suspend fun load(accessToken: String, client: HttpClient) {
        if (loaded) return

        user = getUserData(accessToken, client)
        groups = getGroupData(accessToken, client)

        if (user!!.isSuperAdmin)
            regCodes = getAllRegCodes(accessToken, client)

        if (user!!.isAnyAdmin)
            guiao = getAllGroupsUserIsAdminOf(accessToken, client)

        userList = getUsersByIdentifierList(
            guiao.flatMap { it.members }.distinct(),
            accessToken,
            client
        )

        tagList = getAllTags(accessToken, client)
        
        loaded = true
    }

    fun invalidate() {
        loaded = false
    }
}
