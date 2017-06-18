package info.metadude.android.battery.map

import info.metadude.android.battery.map.models.Scooter
import java.util.*

class ScootersMapState(
        val type: Type,
        val items: List<Scooter>
) {

    enum class Type {
        IDLE,
        LOADING,
        DONE,
        ERROR
    }

    companion object Factory {
        fun createDefault() = ScootersMapState(Type.IDLE, Collections.emptyList())
    }

}
