package orlo.ricknmorty.network.models.remote

import kotlinx.serialization.Serializable
import orlo.ricknmorty.network.models.domain.CharacterPage


@kotlinx.serialization.Serializable
data class RemoteCharacterPage(
    val info: Info,
    val results: List<RemoteCharacter>
) {
    @Serializable
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}

fun RemoteCharacterPage.toDomainCharacterPage(): CharacterPage {
    return CharacterPage(
        info = CharacterPage.Info(
            count = info.count,
            pages = info.pages,
            next = info.next,
            prev = info.next
        ),
        characters = results.map { it.toDomainCharacter() }
    )
}