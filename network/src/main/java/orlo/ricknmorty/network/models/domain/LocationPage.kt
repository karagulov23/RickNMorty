package orlo.ricknmorty.network.models.domain

import orlo.ricknmorty.network.models.domain.EpisodePage.Info

data class LocationPage(
    val info: Info,
    val episodes: List<Episode>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}
