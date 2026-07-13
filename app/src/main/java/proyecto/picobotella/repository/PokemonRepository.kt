package proyecto.picobotella.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class PokemonRepository {

    suspend fun getRandomPokemonImageUrl(): String? = withContext(Dispatchers.IO) {
        try {
            val json = URL(API_URL).readText()
            val pokemonArray = JSONObject(json).getJSONArray("pokemon")
            val randomIndex = (0 until pokemonArray.length()).random()
            val imgUrl = pokemonArray.getJSONObject(randomIndex).getString("img")
            imgUrl.replace("http://", "https://")
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json"
    }
}
