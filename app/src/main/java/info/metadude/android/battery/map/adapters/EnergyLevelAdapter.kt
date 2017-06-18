package info.metadude.android.battery.map.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import info.metadude.android.battery.map.models.EnergyLevel

class EnergyLevelAdapter {

    @FromJson
    fun fromJson(value: Int): EnergyLevel {
        return EnergyLevel(value)
    }

    @ToJson
    fun toJson(energyLevel: EnergyLevel) = energyLevel.numericValue

}
