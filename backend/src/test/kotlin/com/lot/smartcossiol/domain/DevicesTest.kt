package com.lot.smartcossiol.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import com.lot.smartcossiol.web.rest.equalsVerifier

class DevicesTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Devices::class)
        val devices1 = Devices()
        devices1.id = 1L
        val devices2 = Devices()
        devices2.id = devices1.id
        assertThat(devices1).isEqualTo(devices2)
        devices2.id = 2L
        assertThat(devices1).isNotEqualTo(devices2)
        devices1.id = null
        assertThat(devices1).isNotEqualTo(devices2)
    }
}
