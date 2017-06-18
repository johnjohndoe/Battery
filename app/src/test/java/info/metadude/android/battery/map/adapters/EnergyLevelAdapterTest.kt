package info.metadude.android.battery.map.adapters

import info.metadude.android.battery.BaseUnitTest
import info.metadude.android.battery.map.models.EnergyLevel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EnergyLevelAdapterTest : BaseUnitTest() {

    private val adapter = EnergyLevelAdapter()

    @Test
    fun fromJsonWithZero() {
        assertThat(adapter.fromJson(42)).isEqualTo(EnergyLevel(42))
    }

    @Test
    fun toJsonWithZero() {
        assertThat(adapter.toJson(EnergyLevel(42))).isEqualTo(42)
    }

}
