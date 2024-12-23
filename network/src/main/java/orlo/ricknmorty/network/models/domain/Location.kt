package orlo.ricknmorty.network.models.domain

import orlo.ricknmorty.network.models.remote.RemoteCharacter

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimensions: String,
    val residents: List<RemoteCharacter>
)
