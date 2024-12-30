package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

data class PokemonResponse(
    val results: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val url: String
)