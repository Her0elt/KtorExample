package com.example.Repo

import com.example.Entity.Player
import com.example.Types.PlayerType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

class PlayerRepo {
    suspend fun addPlayer(player: PlayerType): PlayerType? {
        var key: UUID? = null
        ODatabase.dbQuery {
            key = (
                Player.insert {
                    it[id] = UUID.randomUUID()
                    it[name] = player.name
                    it[number] = player.number
                    it[team] = player.team
                } get Player.id
                )
        }
        return key?.let { getPlayer(it) }
    }

    suspend fun getAllPlayers(): List<PlayerType> = ODatabase.dbQuery {
        Player.selectAll().map { toPlayer(it) }
    }
    suspend fun getAllPlayersInTeam(id: UUID): List<PlayerType> = ODatabase.dbQuery {
        Player.select { Player.team eq id }.map { toPlayer(it) }
    }

    suspend fun deletePlayer(id: UUID): Boolean {
        return ODatabase.dbQuery {
            Player.deleteWhere { Player.id eq id } > 0
        }
    }

    suspend fun getPlayer(id: UUID): PlayerType? = ODatabase.dbQuery {
        Player.select { (Player.id eq id) }.mapNotNull { toPlayer(it) }.singleOrNull()
    }

    suspend fun updatePlayer(id: UUID, playerData: PlayerType): Boolean {
        return ODatabase.dbQuery {
            Player.update({ Player.id eq id }) {
                it[name] = playerData.name
                it[number] = playerData.number
                it[team] = playerData.team
            } > 0
        }
    }
    private fun toPlayer(row: ResultRow): PlayerType =
        PlayerType(
            id = row[Player.id],
            name = row[Player.name],
            number = row[Player.number],
            team = row[Player.team]
        )
}
