package com.example.Repo

import com.example.Entity.News
import com.example.types.NewsType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

class NewsRepo {
    suspend fun addNews(news: NewsType): NewsType? {
        var key: UUID? = null
        ODatabase.dbQuery {
            key = (
                News.insert {
                    it[id] = UUID.randomUUID()
                    it[title] = news.title
                    it[content] = news.content
                } get News.id
                )
        }
        return key?.let { getNews(it) }
    }

    suspend fun getAllNews(): List<NewsType> = ODatabase.dbQuery {
        News.selectAll().map { toNews(it) }
    }

    suspend fun deleteNews(id: UUID): Boolean {
        return ODatabase.dbQuery {
            News.deleteWhere { News.id eq id } > 0
        }
    }

    suspend fun getNews(id: UUID): NewsType? = ODatabase.dbQuery {
        News.select { (News.id eq id) }.mapNotNull { toNews(it) }.singleOrNull()
    }

    suspend fun updateNews(id: UUID, newsData: NewsType): Boolean {
        return ODatabase.dbQuery {
            News.update({ News.id eq id }) {
                it[title] = newsData.title
                it[content] = newsData.content
            } > 0
        }
    }
    private fun toNews(row: ResultRow): NewsType =
        NewsType(
            id = row[News.id],
            title = row[News.title],
            content = row[News.content]
        )
}
