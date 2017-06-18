package info.metadude.android.battery.map.models

data class EnergyLevel(

        val numericValue: Int

) {

    val status: Status

    init {
        status = numericValueToStatusValue(numericValue)
    }

    enum class Status {
        LOW,
        MEDIUM,
        HIGH,
        UNDEFINED
    }

    private fun numericValueToStatusValue(numericValue: Int): Status {
        if (numericValue in 0..30) {
            return Status.LOW
        }
        if (numericValue in 31..50) {
            return Status.MEDIUM
        }
        if (numericValue in 51..100) {
            return Status.HIGH
        }
        return Status.UNDEFINED
    }

}
