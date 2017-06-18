package info.metadude.android.battery.map.models

import info.metadude.android.battery.map.models.EnergyLevel.Status
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class EnergyLevelTest(
        val numericValue: Int,
        val status: Status
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: numericValue = {0} => {1}")
        fun conditionMatrix(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(-1, Status.UNDEFINED),
                    arrayOf(0, Status.LOW),
                    arrayOf(30, Status.LOW),
                    arrayOf(31, Status.MEDIUM),
                    arrayOf(50, Status.MEDIUM),
                    arrayOf(51, Status.HIGH),
                    arrayOf(100, Status.HIGH),
                    arrayOf(101, Status.UNDEFINED)
            )
        }
    }

    @Test
    fun getStatus() {
        assertThat(EnergyLevel(numericValue).status).isEqualTo(status)
    }

}
