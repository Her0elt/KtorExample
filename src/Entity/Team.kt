package com.example.Entity

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object Team: Table() {
    val id: Column<UUID> = uuid("id")
    val name: Column<String> = varchar("name", 50)
    val country: Column<String> = varchar("country",50)
    override val primaryKey = PrimaryKey(Player.id, name = "pk_team_id")
}

