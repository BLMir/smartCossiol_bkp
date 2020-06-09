package com.lot.smartcossiol.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import com.lot.smartcossiol.web.rest.equalsVerifier

class StatsTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Stats::class)
        val stats1 = Stats()
        stats1.id = 1L
        val stats2 = Stats()
        stats2.id = stats1.id
        assertThat(stats1).isEqualTo(stats2)
        stats2.id = 2L
        assertThat(stats1).isNotEqualTo(stats2)
        stats1.id = null
        assertThat(stats1).isNotEqualTo(stats2)
    }
}
