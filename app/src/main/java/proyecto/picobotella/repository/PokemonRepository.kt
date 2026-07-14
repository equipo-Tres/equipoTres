package proyecto.picobotella.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import proyecto.picobotella.webservice.PokemonApiService

class PokemonRepository(
    private val pokemonApiService: PokemonApiService
) {

    suspend fun getRandomPokemonImageUrl(): String? = withContext(Dispatchers.IO) {
        try {
            pokemonApiService.getRandomPokemonImageUrl()
        } catch (e: Exception) {
            null
        }
    }
}
