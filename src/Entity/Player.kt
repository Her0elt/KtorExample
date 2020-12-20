package com.example.Entity

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object Player: Table() {
    val id: Column<UUID> = uuid("id")
    val name: Column<String> = varchar("name", 50)
    val number: Column<Int> = integer("number")
    val team: Column<UUID?> = (uuid("team") references Team.id).nullable()
    override val primaryKey = PrimaryKey(id, name = "pk_player_id")
}

