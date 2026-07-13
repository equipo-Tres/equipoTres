package proyecto.picobotella.webservice

import proyecto.picobotella.utils.Constants.POKEMON_API_URL

object PokemonApiUtils {

    fun getPokemonApiService(): PokemonApiService {
        return PokemonApiService(POKEMON_API_URL)
    }
}
