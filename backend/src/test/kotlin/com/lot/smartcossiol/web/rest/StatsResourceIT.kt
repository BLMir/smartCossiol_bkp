package com.lot.smartcossiol.web.rest

import com.lot.smartcossiol.SmartCossiolBackendApp
import com.lot.smartcossiol.domain.Stats
import com.lot.smartcossiol.repository.StatsRepository
import com.lot.smartcossiol.web.rest.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.time.Instant
import java.time.temporal.ChronoUnit

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
 * Integration tests for the [StatsResource] REST controller.
 *
 * @see StatsResource
 */
@SpringBootTest(classes = [SmartCossiolBackendApp::class])
class StatsResourceIT {

    @Autowired
    private lateinit var statsRepository: StatsRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restStatsMockMvc: MockMvc

    private lateinit var stats: Stats

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val statsResource = StatsResource(statsRepository)
        this.restStatsMockMvc = MockMvcBuilders.standaloneSetup(statsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        stats = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createStats() {
        val databaseSizeBeforeCreate = statsRepository.findAll().size

        // Create the Stats
        restStatsMockMvc.perform(
            post("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(stats))
        ).andExpect(status().isCreated)

        // Validate the Stats in the database
        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeCreate + 1)
        val testStats = statsList[statsList.size - 1]
        assertThat(testStats.temp).isEqualTo(DEFAULT_TEMP)
        assertThat(testStats.soil).isEqualTo(DEFAULT_SOIL)
        assertThat(testStats.light).isEqualTo(DEFAULT_LIGHT)
        assertThat(testStats.insertAt).isEqualTo(DEFAULT_INSERT_AT)
    }

    @Test
    @Transactional
    fun createStatsWithExistingId() {
        val databaseSizeBeforeCreate = statsRepository.findAll().size

        // Create the Stats with an existing ID
        stats.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatsMockMvc.perform(
            post("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(stats))
        ).andExpect(status().isBadRequest)

        // Validate the Stats in the database
        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkTempIsRequired() {
        val databaseSizeBeforeTest = statsRepository.findAll().size
        // set the field null
        stats.temp = null

        // Create the Stats, which fails.

        restStatsMockMvc.perform(
            post("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(stats))
        ).andExpect(status().isBadRequest)

        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkSoilIsRequired() {
        val databaseSizeBeforeTest = statsRepository.findAll().size
        // set the field null
        stats.soil = null

        // Create the Stats, which fails.

        restStatsMockMvc.perform(
            post("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(stats))
        ).andExpect(status().isBadRequest)

        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkLightIsRequired() {
        val databaseSizeBeforeTest = statsRepository.findAll().size
        // set the field null
        stats.light = null

        // Create the Stats, which fails.

        restStatsMockMvc.perform(
            post("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(stats))
        ).andExpect(status().isBadRequest)

        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkInsertAtIsRequired() {
        val databaseSizeBeforeTest = statsRepository.findAll().size
        // set the field null
        stats.insertAt = null

        // Create the Stats, which fails.

        restStatsMockMvc.perform(
            post("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(stats))
        ).andExpect(status().isBadRequest)

        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllStats() {
        // Initialize the database
        statsRepository.saveAndFlush(stats)

        // Get all the statsList
        restStatsMockMvc.perform(get("/api/stats?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stats.id?.toInt())))
            .andExpect(jsonPath("$.[*].temp").value(hasItem(DEFAULT_TEMP)))
            .andExpect(jsonPath("$.[*].soil").value(hasItem(DEFAULT_SOIL)))
            .andExpect(jsonPath("$.[*].light").value(hasItem(DEFAULT_LIGHT)))
            .andExpect(jsonPath("$.[*].insertAt").value(hasItem(DEFAULT_INSERT_AT.toString())))
    }
    
    @Test
    @Transactional
    fun getStats() {
        // Initialize the database
        statsRepository.saveAndFlush(stats)

        val id = stats.id
        assertNotNull(id)

        // Get the stats
        restStatsMockMvc.perform(get("/api/stats/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.temp").value(DEFAULT_TEMP))
            .andExpect(jsonPath("$.soil").value(DEFAULT_SOIL))
            .andExpect(jsonPath("$.light").value(DEFAULT_LIGHT))
            .andExpect(jsonPath("$.insertAt").value(DEFAULT_INSERT_AT.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingStats() {
        // Get the stats
        restStatsMockMvc.perform(get("/api/stats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateStats() {
        // Initialize the database
        statsRepository.saveAndFlush(stats)

        val databaseSizeBeforeUpdate = statsRepository.findAll().size

        // Update the stats
        val id = stats.id
        assertNotNull(id)
        val updatedStats = statsRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedStats are not directly saved in db
        em.detach(updatedStats)
        updatedStats.temp = UPDATED_TEMP
        updatedStats.soil = UPDATED_SOIL
        updatedStats.light = UPDATED_LIGHT
        updatedStats.insertAt = UPDATED_INSERT_AT

        restStatsMockMvc.perform(
            put("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedStats))
        ).andExpect(status().isOk)

        // Validate the Stats in the database
        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate)
        val testStats = statsList[statsList.size - 1]
        assertThat(testStats.temp).isEqualTo(UPDATED_TEMP)
        assertThat(testStats.soil).isEqualTo(UPDATED_SOIL)
        assertThat(testStats.light).isEqualTo(UPDATED_LIGHT)
        assertThat(testStats.insertAt).isEqualTo(UPDATED_INSERT_AT)
    }

    @Test
    @Transactional
    fun updateNonExistingStats() {
        val databaseSizeBeforeUpdate = statsRepository.findAll().size

        // Create the Stats

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatsMockMvc.perform(
            put("/api/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(stats))
        ).andExpect(status().isBadRequest)

        // Validate the Stats in the database
        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteStats() {
        // Initialize the database
        statsRepository.saveAndFlush(stats)

        val databaseSizeBeforeDelete = statsRepository.findAll().size

        val id = stats.id
        assertNotNull(id)

        // Delete the stats
        restStatsMockMvc.perform(
            delete("/api/stats/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val statsList = statsRepository.findAll()
        assertThat(statsList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_TEMP: Int = 1
        private const val UPDATED_TEMP: Int = 2

        private const val DEFAULT_SOIL: Int = 1
        private const val UPDATED_SOIL: Int = 2

        private const val DEFAULT_LIGHT: Int = 1
        private const val UPDATED_LIGHT: Int = 2

        private val DEFAULT_INSERT_AT: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_INSERT_AT: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Stats {
            val stats = Stats(
                temp = DEFAULT_TEMP,
                soil = DEFAULT_SOIL,
                light = DEFAULT_LIGHT,
                insertAt = DEFAULT_INSERT_AT
            )

            return stats
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Stats {
            val stats = Stats(
                temp = UPDATED_TEMP,
                soil = UPDATED_SOIL,
                light = UPDATED_LIGHT,
                insertAt = UPDATED_INSERT_AT
            )

            return stats
        }
    }
}
