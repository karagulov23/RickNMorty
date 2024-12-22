package orlo.ricknmorty.network.models.remote

import kotlinx.serialization.Serializable
import orlo.ricknmorty.network.models.domain.EpisodePage

@Serializable
data class RemoteEpisodePage(
    val info: Info,
    val results: List<RemoteEpisode>
) {
    @Serializable
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}

fun RemoteEpisodePage.toDomainEpisodePage(): EpisodePage {
    return EpisodePage(
        info = EpisodePage.Info(
            count = info.count,
            pages = info.pages,
            next = info.next,
            prev = info.prev
        ),
        episodes = results.map { it.toDomainEpisode() }
    )
}