package com.example.entity

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object News : Table() {
    val id: Column<UUID> = uuid("id")
    val title: Column<String> = varchar("title", 50)
    val content: Column<String> = varchar("content", 1000)
}
