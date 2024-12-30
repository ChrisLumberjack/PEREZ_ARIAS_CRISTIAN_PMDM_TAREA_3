package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonService {
    @GET("pokemon?offset=0&limit=150")
    public fun getPokemonList(): Call<PokemonResponse>

    @GET
    fun getPokemonDetails(@Url url: String): Call<PokemonDetailResponse>
}
