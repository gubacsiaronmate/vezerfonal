package com.smokinggunstudio.vezerfonal.network.helpers

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.data.OrgData
import io.ktor.http.HttpStatusCode

object NetworkConstants {
    const val BASE_URL = "https://api.vezerfonal.org"
    
    object Endpoints {
        /** POST: /register/basic -> success: [HttpStatusCode.Created] + id: [Int] */
        const val REGISTER_DATA_BASIC = "/register/basic"
        /** POST: /register/basic/pfp/{userId}/{rememberMe} -> tokens: [TokenResponse] */
        const val REGISTER_PICTURE = "/register/basic/pfp/"
        /** POST: /login/basic -> tokens: [TokenResponse] */
        const val LOGIN_BASIC = "/login/basic"
        /** GET: /api/messages/{amount} -> messages: [List]<[MessageData]> */
        const val GET_MESSAGES = "/api/messages/"
        /** POST: /api/messages/send */
        const val SEND_MESSAGE = "/api/messages/send"
        /** GET: /api */
        const val AUTH_CHECKER = "/api"
        /** GET: /refresh */
        const val REFRESH_REQUEST = "/refresh"
        /** GET: /api/user-data -> userData: [UserData] */
        const val GET_USER_DATA = "/api/users/data"
        /** GET: /api/group-data -> groupData: [List]<[GroupData]> */
        const val GET_GROUP_DATA = "/api/groups/data"
        /** GET: /api/logout */
        const val LOGOUT = "/api/logout"
        /** POST: /register/create-org */
        const val CREATE_ORG = "/register/create-org"
        /** GET: /organisations -> [List]<[OrgData]> */
        const val GET_ORGS = "/organisations"
        /** POST: /api/code/create */
        const val CREATE_CODE = "/api/codes/create"
        /** POST: /api/group/join */
        const val JOIN_GROUP = "/api/groups/join"
        
        const val GET_ALL_USERS = "/api/users/all"
        
        const val CREATE_GROUP = "/api/groups/create"
        
        const val GET_ALL_REG_CODES = "/api/codes/all"
        
        const val GET_ALL_GROUPS_USER_IS_ADMIN_OF = "/api/groups/im-admin-of"
        
        const val GET_USERS_BY_IDENTIFIER_LIST = "/api/users/by-identifier-list"
        
        const val GET_ALL_TAGS = "/api/tags/all"
        
        const val SUBSCRIBE_TO_MESSAGES = "/api/messages/subscribe"
        
        const val INTERACTIONS = "/api/messages/interactions"
        
        const val GET_ARCHIVED = "/api/messages/archived/"
        
        const val GET_SENT_MESSAGES = "/api/messages/sent/"
        
        const val PATCH_REG_CODE = "/api/codes/update"
        
        const val DELETE_REG_CODE = "/api/codes/delete"
        
        const val TAG_CREATE = "/api/tags/create"
        
        const val TAG_UPDATE = "/api/tags/update"
        
        const val TAG_DELETE = "/api/tags/delete"
    }
}