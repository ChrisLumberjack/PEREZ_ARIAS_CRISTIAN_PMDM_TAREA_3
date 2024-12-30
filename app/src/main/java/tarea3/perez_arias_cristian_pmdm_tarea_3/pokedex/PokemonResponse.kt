package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

data class PokemonResponse(
    val results: List<Pokemon>
)

data class Pokemon(
    val id: Int = 0,
    var photoUrl: String? = null,
    val name: String = "",
    val url: String = "",
    val userEmail: String = "",
    val types: List<String> = emptyList()  // Lista vacía como valor predeterminado
)

data class Type(
    val type: TypeDetail
)

data class TypeDetail(
    val name: String
)
