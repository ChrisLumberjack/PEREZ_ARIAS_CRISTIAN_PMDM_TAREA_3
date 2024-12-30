package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

data class PokemonDetailResponse(
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<Ability>
)

data class Ability(
    val ability: AbilityDetail
)

data class AbilityDetail(
    val name: String
)
