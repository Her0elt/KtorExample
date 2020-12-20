package com.example.Route

import com.example.Repo.PlayerRepo
import com.example.Types.PlayerType
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import java.lang.IllegalStateException
import java.util.UUID

fun Route.PlayerRoute(PlayerRepo: PlayerRepo) {
    route("/player") {
        get("/") {
            call.respond(PlayerRepo.getAllPlayers())
        }
        get("/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"]) ?: throw IllegalStateException("Must provide id")
            PlayerRepo.getPlayer(id)?.let { news -> call.respond(news) }
        }
        post("/") {
            val playerData = call.receive<PlayerType>()
            val newPlayer = PlayerRepo.addPlayer(playerData)
            call.respond(HttpStatusCode.Created, newPlayer!!)
        }
        put("/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"]) ?: throw IllegalStateException("Must provide id")
            val updateData = call.receive<PlayerType>()
            val isUpdated = if (PlayerRepo.getPlayer(id) != null) {
                PlayerRepo.updatePlayer(id, updateData)
            } else false
            if (isUpdated) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}