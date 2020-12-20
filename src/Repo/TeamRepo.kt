package com.example.Repo

import com.example.Entity.Team
import com.example.Types.TeamPlayersType
import com.example.Types.TeamType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

class TeamRepo {
    suspend fun addTeam(team: TeamType): TeamPlayersType? {
        var key: UUID? = null
        ODatabase.dbQuery {
            key = (
                Team.insert {
                    it[id] = UUID.randomUUID()
                    it[name] = team.name
                    it[country] = team.country
                } get Team.id
                )
        }
        return key?.let { getTeam(it) }
    }

    suspend fun getAllTeams(): List<TeamType> = ODatabase.dbQuery {
        Team.selectAll().map { toTeam(it) }
    }

    suspend fun deleteTeam(id: UUID): Boolean {
        return ODatabase.dbQuery {
            Team.deleteWhere { Team.id eq id } > 0
        }
    }

    suspend fun getTeam(id: UUID): TeamPlayersType? = ODatabase.dbQuery {
        Team.select { (Team.id eq id) }.mapNotNull { toTeamPlayer(it) }.singleOrNull()
    }

    suspend fun updateTeam(id: UUID, teamData: TeamType): Boolean {
        return ODatabase.dbQuery {
            Team.update({ Team.id eq id }) {
                it[name] = teamData.name
                it[country] = teamData.country
            } > 0
        }
    }
    private fun toTeam(row: ResultRow): TeamType =
        TeamType(
            id = row[Team.id],
            name = row[Team.name],
            country = row[Team.country]
        )

    private suspend fun toTeamPlayer(row: ResultRow): TeamPlayersType =
        TeamPlayersType(
            id = row[Team.id],
            name = row[Team.name],
            country = row[Team.country],
            players = ODatabase.dbQuery { PlayerRepo().getAllPlayersInTeam(row[Team.id]) }
        )
}
