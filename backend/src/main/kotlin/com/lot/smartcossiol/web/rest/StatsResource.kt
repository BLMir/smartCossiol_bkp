package com.lot.smartcossiol.web.rest

import com.lot.smartcossiol.domain.Stats
import com.lot.smartcossiol.repository.StatsRepository
import com.lot.smartcossiol.web.rest.errors.BadRequestAlertException

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid
import java.net.URI
import java.net.URISyntaxException

private const val ENTITY_NAME = "stats"
/**
 * REST controller for managing [com.lot.smartcossiol.domain.Stats].
 */
@RestController
@RequestMapping("/api")
@Transactional
class StatsResource(
    private val statsRepository: StatsRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /stats` : Create a new stats.
     *
     * @param stats the stats to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new stats, or with status `400 (Bad Request)` if the stats has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stats")
    fun createStats(@Valid @RequestBody stats: Stats): ResponseEntity<Stats> {
        log.debug("REST request to save Stats : {}", stats)
        if (stats.id != null) {
            throw BadRequestAlertException(
                "A new stats cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = statsRepository.save(stats)
        return ResponseEntity.created(URI("/api/stats/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /stats` : Updates an existing stats.
     *
     * @param stats the stats to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated stats,
     * or with status `400 (Bad Request)` if the stats is not valid,
     * or with status `500 (Internal Server Error)` if the stats couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stats")
    fun updateStats(@Valid @RequestBody stats: Stats): ResponseEntity<Stats> {
        log.debug("REST request to update Stats : {}", stats)
        if (stats.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = statsRepository.save(stats)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, false, ENTITY_NAME,
                     stats.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /stats` : get all the stats.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of stats in body.
     */
    @GetMapping("/stats")    
    fun getAllStats(): MutableList<Stats> {
        log.debug("REST request to get all Stats")
        return statsRepository.findAll()
    }

    /**
     * `GET  /stats/:id` : get the "id" stats.
     *
     * @param id the id of the stats to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the stats, or with status `404 (Not Found)`.
     */
    @GetMapping("/stats/{id}")
    fun getStats(@PathVariable id: Long): ResponseEntity<Stats> {
        log.debug("REST request to get Stats : {}", id)
        val stats = statsRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(stats)
    }
    /**
     *  `DELETE  /stats/:id` : delete the "id" stats.
     *
     * @param id the id of the stats to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/stats/{id}")
    fun deleteStats(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Stats : {}", id)

        statsRepository.deleteById(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build()
    }
}
