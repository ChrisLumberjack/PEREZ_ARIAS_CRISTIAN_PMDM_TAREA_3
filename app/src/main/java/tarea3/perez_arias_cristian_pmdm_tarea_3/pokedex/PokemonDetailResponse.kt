package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

data class PokemonDetailResponse(
    val name: String,
    val id: Int, // Índice del Pokémon en la Pokédex
    val height: Int,
    val weight: Int,
    val sprites: Sprites, // Para la imagen del Pokémon
    val types: List<TypeSlot>, // Lista de tipos del Pokémon
    val abilities: List<Ability>, // Habilidades del Pokémon
)

data class Sprites(
    val front_default: String // URL de la imagen del Pokémon
)

data class TypeSlot(
    val type: AbilityDetail // Cada tipo tiene un nombre
)

data class Ability(
    val ability: AbilityDetail
)

data class AbilityDetail(
    val name: String
)
