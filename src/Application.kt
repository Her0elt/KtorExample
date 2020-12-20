package com.example

import com.example.Repo.NewsRepo
import com.example.Repo.ODatabase
import com.example.Repo.PlayerRepo
import com.example.Repo.TeamRepo
import com.example.Route.NewsRoute
import com.example.Route.PlayerRoute
import com.example.Route.TeamRoute
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.routing.Routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
//@kotlin.jvm.JvmOverloads
fun Application.module() {
    HttpClient(Apache) {
        install(ContentNegotiation) {
            gson {
            }
        }
        ODatabase.init()
        install(Routing) {
            NewsRoute(NewsRepo())
            PlayerRoute(PlayerRepo())
            TeamRoute(TeamRepo())
        }
    }
}
