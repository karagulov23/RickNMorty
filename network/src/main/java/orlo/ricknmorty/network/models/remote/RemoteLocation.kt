package orlo.ricknmorty.network.models.remote

import kotlinx.serialization.Serializable
import orlo.ricknmorty.network.models.domain.Character
import orlo.ricknmorty.network.models.domain.CharacterGender
import orlo.ricknmorty.network.models.domain.CharacterStatus
import orlo.ricknmorty.network.models.domain.Location


@Serializable
data class RemoteLocation(
    val id: Int,
    val name: String,
    val type: String,
    val dimensions: String,
    val residents: List<RemoteCharacter>
)

fun RemoteLocation.toDomainLocation(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimensions = dimensions,
        residents = residents
    )
}