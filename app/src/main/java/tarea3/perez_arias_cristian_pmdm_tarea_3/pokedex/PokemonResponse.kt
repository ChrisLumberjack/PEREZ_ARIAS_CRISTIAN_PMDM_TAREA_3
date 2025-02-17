package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.PropertyName

data class PokemonResponse(
    val results: List<Pokemon>,
)

data class Pokemon(
    var id: Int = 0,
    val url: String = "",
    var photoUrl: String? = null,
    val height: Double = 0.0,
    val weight: Double = 0.0,
    var name: String = "",

    var types: List<String> = listOf(),

    @PropertyName("userEmail") var userEmail: String = "", // Mapeamos "userEmail" a Firestore
    val index: Int = 0,
) : Parcelable {

    // Constructor vacío necesario para Firebase y otros lugares donde sea necesario
    constructor() : this(0, "", null, 0.0, 0.0, "", listOf(), "")

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: arrayListOf(),
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(url)
        parcel.writeString(photoUrl)
        parcel.writeDouble(height)
        parcel.writeDouble(weight)
        parcel.writeString(name)
        parcel.writeStringList(types)
        parcel.writeString(userEmail)
        parcel.writeInt(index)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pokemon> {
        override fun createFromParcel(parcel: Parcel): Pokemon {
            return Pokemon(parcel)
        }

        override fun newArray(size: Int): Array<Pokemon?> {
            return arrayOfNulls(size)
        }
    }
}

// Asegúrate de que el modelo esté bien definido
data class PokemonDetails(
    val id: Int = 0,
    val name: String = "",
    val url: String = "",
    val sprites: Sprites,  // Asegúrate de tener esta clase correctamente definida
    val types: List<Type>,  // Lista de tipos
    val weight: Int = 0,
    val height: Int = 0,
    val index: Int = 0,
)

data class CapturedPokemon(
    val name: String,
    val index: Int,
    val photoUrl: String,
    val types: List<String>,
    val weight: Int,
    val height: Int,
    val userEmail: String,

)

data class Sprites(
    val front_default: String?,  // Imagen del sprite
)

data class Type(
    val type: TypeDetail,
)

data class TypeDetail(
    val name: String,  // Nombre del tipo
)
