package com.example.type

import java.util.UUID

data class TeamType(val id: UUID, val name: String, val country: String)

data class TeamPlayersType(val id: UUID, val name: String, val country: String, val players: List<PlayerType>)
