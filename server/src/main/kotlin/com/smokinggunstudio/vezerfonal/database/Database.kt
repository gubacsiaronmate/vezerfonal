package com.smokinggunstudio.vezerfonal.database

import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/vezerfonal",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = ""
    )
}