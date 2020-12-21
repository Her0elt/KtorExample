package com.example.repo

import com.example.entity.News
import com.example.entity.Player
import com.example.entity.Team
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object ODatabase {
    fun init() {
        Database.connect(hikari())
        transaction {
            create(News)
            create(Player)
            create(Team)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "com.mysql.cj.jdbc.Driver"
        config.jdbcUrl = "jdbc:mysql://db:3306/test" // getSystemVariable("DATABASE_URL")
        config.username = "root" // getSystemVariable("DATABASE_USERNAME")
        config.password = "secret" // getSystemVariable("DATABASE_PASSWORD")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
        block: suspend () -> T
    ): T =
        newSuspendedTransaction { block() }
}
