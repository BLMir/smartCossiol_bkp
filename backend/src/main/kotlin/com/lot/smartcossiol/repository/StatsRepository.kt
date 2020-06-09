package com.lot.smartcossiol.repository

import com.lot.smartcossiol.domain.Stats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Stats] entity.
 */
@Suppress("unused")
@Repository
interface StatsRepository : JpaRepository<Stats, Long> {
}
