package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {
    @GET("pokemon?offset=0&limit=150")
    fun getPokemonList(): Call<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetails(@Path("id") id: Int): Call<PokemonDetails>
}
