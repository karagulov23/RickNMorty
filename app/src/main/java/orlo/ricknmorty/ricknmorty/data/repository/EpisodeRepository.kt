package orlo.ricknmorty.ricknmorty.data.repository

import kotlinx.coroutines.coroutineScope
import orlo.ricknmorty.network.ApiOperation
import orlo.ricknmorty.network.KtorClient
import orlo.ricknmorty.network.models.domain.Episode
import javax.inject.Inject

class EpisodesRepository @Inject constructor(private val ktorClient: KtorClient) {
    suspend fun fetchAllEpisodes(): ApiOperation<List<Episode>> = ktorClient.getAllEpisodes()
    suspend fun fetchEpisode(episodeId: Int): ApiOperation<Episode> = ktorClient.getEpisode(episodeId)
}