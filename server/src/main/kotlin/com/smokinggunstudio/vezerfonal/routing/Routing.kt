package com.smokinggunstudio.vezerfonal.routing

import com.smokinggunstudio.vezerfonal.helpers.ImageService
import com.smokinggunstudio.vezerfonal.helpers.NotificationService.initialize
import com.smokinggunstudio.vezerfonal.routing.api.*
import com.smokinggunstudio.vezerfonal.routing.auth.jwtRefresh
import com.smokinggunstudio.vezerfonal.routing.auth.loginRoute
import com.smokinggunstudio.vezerfonal.routing.auth.registerRoute
import com.smokinggunstudio.vezerfonal.security.auth.configureBasicAuth
import com.smokinggunstudio.vezerfonal.security.auth.configureJWTAuth
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.Configuration
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.CORSConfig
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureRouting(
    imageService: ImageService,
    mainDB: Database
) {
    install(ContentNegotiation, Configuration::json)
    install(CORS, CORSConfig::initialize)
    
    authentication {
        configureBasicAuth(mainDB)
        configureJWTAuth(mainDB)
    }
    
    routing {
        get("/organisations") {
            organisationRoute(mainDB)
        }
        
        route("/register") {
            registerRoute(imageService, mainDB)
        }
        
        authenticate("jwt-refresh") {
            get("/refresh") {
                jwtRefresh(mainDB)
            }
        }
        
        authenticate("basic") {
            route("/login") {
                loginRoute(mainDB)
            }
        }
        
        authenticate("jwt-access") {
            route("/api") {
                get(RoutingContext::apiGetRoute)
                
                get("/logout", RoutingContext::logoutRoute)
                
                route("/messages", Route::messageRoute)
                
                route("/users", Route::userRoute)
                
                route("/groups", Route::groupRoute)
                
                route("/codes") { codeRoute(mainDB) }
                
                route("/tags", Route::tagRoute)
            }
        }
    }
}
