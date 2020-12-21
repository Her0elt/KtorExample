package com.example.route

import com.example.repo.TeamRepo
import com.example.type.TeamType
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

fun Route.TeamRoute(TeamRepo: TeamRepo) {
    route("/team") {
        get("/") {
            call.respond(TeamRepo.getAllTeams())
        }
        get("/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"]) ?: throw IllegalStateException("Must provide id")
            TeamRepo.getTeam(id)?.let { team -> call.respond(team) }
        }
        post("/") {
            val teamData = call.receive<TeamType>()
            val newteam = TeamRepo.addTeam(teamData)
            call.respond(HttpStatusCode.Created, newteam!!)
        }
        put("/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"]) ?: throw IllegalStateException("Must provide id")
            val updateData = call.receive<TeamType>()
            val isUpdated = if (TeamRepo.getTeam(id) != null) {
                TeamRepo.updateTeam(id, updateData)
            } else false
            if (isUpdated) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}
