package proyecto.picobotella.webservice

import org.json.JSONObject
import java.net.URL

class PokemonApiService(
    private val apiUrl: String
) {

    fun getRandomPokemonImageUrl(): String {
        val json = URL(apiUrl).readText()
        val pokemonArray = JSONObject(json).getJSONArray("pokemon")
        val randomIndex = (0 until pokemonArray.length()).random()
        val imageUrl = pokemonArray.getJSONObject(randomIndex).getString("img")
        return imageUrl.replace("http://", "https://")
    }
}
