package info.metadude.android.battery.map.models

import com.squareup.moshi.Json

data class Scooter(

        @Json(name = "energy_level")
        val energyLevel: EnergyLevel,

        val model: String,

        @Json(name = "license_plate")
        val licensePlate: String,

        val location: Location

)
