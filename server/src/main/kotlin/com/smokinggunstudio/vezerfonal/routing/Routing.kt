package com.smokinggunstudio.vezerfonal.routing

import com.smokinggunstudio.vezerfonal.helpers.ImageService
import com.smokinggunstudio.vezerfonal.routing.api.*
import com.smokinggunstudio.vezerfonal.routing.auth.jwtRefresh
import com.smokinggunstudio.vezerfonal.routing.auth.loginRoute
import com.smokinggunstudio.vezerfonal.routing.auth.registerRoute
import com.smokinggunstudio.vezerfonal.security.auth.configureBasicAuth
import com.smokinggunstudio.vezerfonal.security.auth.configureJWTAuth
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.coroutines.CoroutineContext

fun Application.configureRouting(imageService: ImageService, mainDB: Database, context: CoroutineContext) {
    install(ContentNegotiation) { json() }
    
    authentication {
        configureBasicAuth(mainDB, context)
        configureJWTAuth(mainDB, context)
    }
    
    routing {
        route("{...}") {
            handle {
            
            }
        }
        
        get("/") { call.respondText("Hello") }
        
        get("/organisations") {
            organisationRoute(mainDB)
        }
        
        route("/register") {
            registerRoute(imageService, context, mainDB)
        }
        
        authenticate("jwt-refresh") {
            get("/refresh") {
                jwtRefresh(context, mainDB)
            }
        }
        
        authenticate("basic") {
            route("/login") {
                loginRoute(context, mainDB)
            }
        }
        
        authenticate("jwt-access") {
            route("/api") {
                get(RoutingContext::apiGetRoute)
                
                get("/logout", RoutingContext::logoutRoute)
                
                route("/messages") {
                    messageRoute(context)
                }
                
                route("/users", Route::userRoute)
                
                route("/groups") {
                    groupRoute(context)
                }
                
                route("/codes") {
                    codeRoute(context, mainDB)
                }
                
                route("/tags", Route::tagRoute)
            }
        }
    }
}
