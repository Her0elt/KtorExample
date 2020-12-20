package com.example.Route

import com.example.Repo.NewsRepo
import com.example.types.NewsType
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

fun Route.NewsRoute(NewsRepo: NewsRepo) {
    route("/news") {
        get("/") {
            call.respond(NewsRepo.getAllNews())
        }
        get("/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"]) ?: throw IllegalStateException("Must provide id")
            NewsRepo.getNews(id)?.let { news -> call.respond(news) }
        }
        post("/") {
            val newsData = call.receive<NewsType>()
            val newNews = NewsRepo.addNews(newsData)
            call.respond(HttpStatusCode.Created, newNews!!)
        }
        put("/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"]) ?: throw IllegalStateException("Must provide id")
            val updateData = call.receive<NewsType>()
            val isUpdated = if (NewsRepo.getNews(id) != null) {
                NewsRepo.updateNews(id, updateData)
            } else false
            if (isUpdated) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}
