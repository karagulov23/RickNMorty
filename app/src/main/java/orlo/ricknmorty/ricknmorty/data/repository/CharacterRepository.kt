package orlo.ricknmorty.ricknmorty.data.repository

import orlo.ricknmorty.network.ApiOperation
import orlo.ricknmorty.network.KtorClient
import orlo.ricknmorty.network.models.domain.Character
import orlo.ricknmorty.network.models.domain.CharacterPage
import javax.inject.Inject


class CharacterRepository @Inject constructor(
    private val ktorClient: KtorClient
) {

    suspend fun fetchCharacterPage(page: Int): ApiOperation<CharacterPage> {
        return ktorClient.getCharacterByPage(page)
    }

    suspend fun fetchCharacter(characterId: Int): ApiOperation<Character> {
        return ktorClient.getCharacter(characterId)
    }

    suspend fun fetchAllCharactersByName(searchQuery: String){
    }
}

