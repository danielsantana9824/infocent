package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cifrado;
import com.mycompany.myapp.repository.CifradoRepository;
import com.mycompany.myapp.service.criteria.CifradoCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CifradoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CifradoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_CIFRADO = "AAAAAAAAAA";
    private static final String UPDATED_CIFRADO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_VENCIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/cifrados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CifradoRepository cifradoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCifradoMockMvc;

    private Cifrado cifrado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cifrado createEntity(EntityManager em) {
        Cifrado cifrado = new Cifrado()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .cifrado(DEFAULT_CIFRADO)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO);
        return cifrado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cifrado createUpdatedEntity(EntityManager em) {
        Cifrado cifrado = new Cifrado()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .cifrado(UPDATED_CIFRADO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO);
        return cifrado;
    }

    @BeforeEach
    public void initTest() {
        cifrado = createEntity(em);
    }

    @Test
    @Transactional
    void createCifrado() throws Exception {
        int databaseSizeBeforeCreate = cifradoRepository.findAll().size();
        // Create the Cifrado
        restCifradoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cifrado)))
            .andExpect(status().isCreated());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeCreate + 1);
        Cifrado testCifrado = cifradoList.get(cifradoList.size() - 1);
        assertThat(testCifrado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCifrado.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCifrado.getCifrado()).isEqualTo(DEFAULT_CIFRADO);
        assertThat(testCifrado.getFechaVencimiento()).isEqualTo(DEFAULT_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void createCifradoWithExistingId() throws Exception {
        // Create the Cifrado with an existing ID
        cifrado.setId(1L);

        int databaseSizeBeforeCreate = cifradoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCifradoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cifrado)))
            .andExpect(status().isBadRequest());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cifradoRepository.findAll().size();
        // set the field null
        cifrado.setNombre(null);

        // Create the Cifrado, which fails.

        restCifradoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cifrado)))
            .andExpect(status().isBadRequest());

        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cifradoRepository.findAll().size();
        // set the field null
        cifrado.setDescripcion(null);

        // Create the Cifrado, which fails.

        restCifradoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cifrado)))
            .andExpect(status().isBadRequest());

        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCifradoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cifradoRepository.findAll().size();
        // set the field null
        cifrado.setCifrado(null);

        // Create the Cifrado, which fails.

        restCifradoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cifrado)))
            .andExpect(status().isBadRequest());

        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCifrados() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList
        restCifradoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cifrado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].cifrado").value(hasItem(DEFAULT_CIFRADO)))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())));
    }

    @Test
    @Transactional
    void getCifrado() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get the cifrado
        restCifradoMockMvc
            .perform(get(ENTITY_API_URL_ID, cifrado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cifrado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.cifrado").value(DEFAULT_CIFRADO))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()));
    }

    @Test
    @Transactional
    void getCifradosByIdFiltering() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        Long id = cifrado.getId();

        defaultCifradoShouldBeFound("id.equals=" + id);
        defaultCifradoShouldNotBeFound("id.notEquals=" + id);

        defaultCifradoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCifradoShouldNotBeFound("id.greaterThan=" + id);

        defaultCifradoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCifradoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCifradosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where nombre equals to DEFAULT_NOMBRE
        defaultCifradoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the cifradoList where nombre equals to UPDATED_NOMBRE
        defaultCifradoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCifradosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where nombre not equals to DEFAULT_NOMBRE
        defaultCifradoShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the cifradoList where nombre not equals to UPDATED_NOMBRE
        defaultCifradoShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCifradosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultCifradoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the cifradoList where nombre equals to UPDATED_NOMBRE
        defaultCifradoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCifradosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where nombre is not null
        defaultCifradoShouldBeFound("nombre.specified=true");

        // Get all the cifradoList where nombre is null
        defaultCifradoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllCifradosByNombreContainsSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where nombre contains DEFAULT_NOMBRE
        defaultCifradoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the cifradoList where nombre contains UPDATED_NOMBRE
        defaultCifradoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCifradosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where nombre does not contain DEFAULT_NOMBRE
        defaultCifradoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the cifradoList where nombre does not contain UPDATED_NOMBRE
        defaultCifradoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCifradosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where descripcion equals to DEFAULT_DESCRIPCION
        defaultCifradoShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the cifradoList where descripcion equals to UPDATED_DESCRIPCION
        defaultCifradoShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCifradosByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultCifradoShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the cifradoList where descripcion not equals to UPDATED_DESCRIPCION
        defaultCifradoShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCifradosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultCifradoShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the cifradoList where descripcion equals to UPDATED_DESCRIPCION
        defaultCifradoShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCifradosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where descripcion is not null
        defaultCifradoShouldBeFound("descripcion.specified=true");

        // Get all the cifradoList where descripcion is null
        defaultCifradoShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCifradosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where descripcion contains DEFAULT_DESCRIPCION
        defaultCifradoShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the cifradoList where descripcion contains UPDATED_DESCRIPCION
        defaultCifradoShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCifradosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultCifradoShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the cifradoList where descripcion does not contain UPDATED_DESCRIPCION
        defaultCifradoShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCifradosByCifradoIsEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where cifrado equals to DEFAULT_CIFRADO
        defaultCifradoShouldBeFound("cifrado.equals=" + DEFAULT_CIFRADO);

        // Get all the cifradoList where cifrado equals to UPDATED_CIFRADO
        defaultCifradoShouldNotBeFound("cifrado.equals=" + UPDATED_CIFRADO);
    }

    @Test
    @Transactional
    void getAllCifradosByCifradoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where cifrado not equals to DEFAULT_CIFRADO
        defaultCifradoShouldNotBeFound("cifrado.notEquals=" + DEFAULT_CIFRADO);

        // Get all the cifradoList where cifrado not equals to UPDATED_CIFRADO
        defaultCifradoShouldBeFound("cifrado.notEquals=" + UPDATED_CIFRADO);
    }

    @Test
    @Transactional
    void getAllCifradosByCifradoIsInShouldWork() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where cifrado in DEFAULT_CIFRADO or UPDATED_CIFRADO
        defaultCifradoShouldBeFound("cifrado.in=" + DEFAULT_CIFRADO + "," + UPDATED_CIFRADO);

        // Get all the cifradoList where cifrado equals to UPDATED_CIFRADO
        defaultCifradoShouldNotBeFound("cifrado.in=" + UPDATED_CIFRADO);
    }

    @Test
    @Transactional
    void getAllCifradosByCifradoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where cifrado is not null
        defaultCifradoShouldBeFound("cifrado.specified=true");

        // Get all the cifradoList where cifrado is null
        defaultCifradoShouldNotBeFound("cifrado.specified=false");
    }

    @Test
    @Transactional
    void getAllCifradosByCifradoContainsSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where cifrado contains DEFAULT_CIFRADO
        defaultCifradoShouldBeFound("cifrado.contains=" + DEFAULT_CIFRADO);

        // Get all the cifradoList where cifrado contains UPDATED_CIFRADO
        defaultCifradoShouldNotBeFound("cifrado.contains=" + UPDATED_CIFRADO);
    }

    @Test
    @Transactional
    void getAllCifradosByCifradoNotContainsSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where cifrado does not contain DEFAULT_CIFRADO
        defaultCifradoShouldNotBeFound("cifrado.doesNotContain=" + DEFAULT_CIFRADO);

        // Get all the cifradoList where cifrado does not contain UPDATED_CIFRADO
        defaultCifradoShouldBeFound("cifrado.doesNotContain=" + UPDATED_CIFRADO);
    }

    @Test
    @Transactional
    void getAllCifradosByFechaVencimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where fechaVencimiento equals to DEFAULT_FECHA_VENCIMIENTO
        defaultCifradoShouldBeFound("fechaVencimiento.equals=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the cifradoList where fechaVencimiento equals to UPDATED_FECHA_VENCIMIENTO
        defaultCifradoShouldNotBeFound("fechaVencimiento.equals=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCifradosByFechaVencimientoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where fechaVencimiento not equals to DEFAULT_FECHA_VENCIMIENTO
        defaultCifradoShouldNotBeFound("fechaVencimiento.notEquals=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the cifradoList where fechaVencimiento not equals to UPDATED_FECHA_VENCIMIENTO
        defaultCifradoShouldBeFound("fechaVencimiento.notEquals=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCifradosByFechaVencimientoIsInShouldWork() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where fechaVencimiento in DEFAULT_FECHA_VENCIMIENTO or UPDATED_FECHA_VENCIMIENTO
        defaultCifradoShouldBeFound("fechaVencimiento.in=" + DEFAULT_FECHA_VENCIMIENTO + "," + UPDATED_FECHA_VENCIMIENTO);

        // Get all the cifradoList where fechaVencimiento equals to UPDATED_FECHA_VENCIMIENTO
        defaultCifradoShouldNotBeFound("fechaVencimiento.in=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCifradosByFechaVencimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where fechaVencimiento is not null
        defaultCifradoShouldBeFound("fechaVencimiento.specified=true");

        // Get all the cifradoList where fechaVencimiento is null
        defaultCifradoShouldNotBeFound("fechaVencimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllCifradosByFechaVencimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where fechaVencimiento is greater than or equal to DEFAULT_FECHA_VENCIMIENTO
        defaultCifradoShouldBeFound("fechaVencimiento.greaterThanOrEqual=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the cifradoList where fechaVencimiento is greater than or equal to UPDATED_FECHA_VENCIMIENTO
        defaultCifradoShouldNotBeFound("fechaVencimiento.greaterThanOrEqual=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCifradosByFechaVencimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where fechaVencimiento is less than or equal to DEFAULT_FECHA_VENCIMIENTO
        defaultCifradoShouldBeFound("fechaVencimiento.lessThanOrEqual=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the cifradoList where fechaVencimiento is less than or equal to SMALLER_FECHA_VENCIMIENTO
        defaultCifradoShouldNotBeFound("fechaVencimiento.lessThanOrEqual=" + SMALLER_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCifradosByFechaVencimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where fechaVencimiento is less than DEFAULT_FECHA_VENCIMIENTO
        defaultCifradoShouldNotBeFound("fechaVencimiento.lessThan=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the cifradoList where fechaVencimiento is less than UPDATED_FECHA_VENCIMIENTO
        defaultCifradoShouldBeFound("fechaVencimiento.lessThan=" + UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void getAllCifradosByFechaVencimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        // Get all the cifradoList where fechaVencimiento is greater than DEFAULT_FECHA_VENCIMIENTO
        defaultCifradoShouldNotBeFound("fechaVencimiento.greaterThan=" + DEFAULT_FECHA_VENCIMIENTO);

        // Get all the cifradoList where fechaVencimiento is greater than SMALLER_FECHA_VENCIMIENTO
        defaultCifradoShouldBeFound("fechaVencimiento.greaterThan=" + SMALLER_FECHA_VENCIMIENTO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCifradoShouldBeFound(String filter) throws Exception {
        restCifradoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cifrado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].cifrado").value(hasItem(DEFAULT_CIFRADO)))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())));

        // Check, that the count call also returns 1
        restCifradoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCifradoShouldNotBeFound(String filter) throws Exception {
        restCifradoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCifradoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCifrado() throws Exception {
        // Get the cifrado
        restCifradoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCifrado() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();

        // Update the cifrado
        Cifrado updatedCifrado = cifradoRepository.findById(cifrado.getId()).get();
        // Disconnect from session so that the updates on updatedCifrado are not directly saved in db
        em.detach(updatedCifrado);
        updatedCifrado
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .cifrado(UPDATED_CIFRADO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO);

        restCifradoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCifrado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCifrado))
            )
            .andExpect(status().isOk());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
        Cifrado testCifrado = cifradoList.get(cifradoList.size() - 1);
        assertThat(testCifrado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCifrado.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCifrado.getCifrado()).isEqualTo(UPDATED_CIFRADO);
        assertThat(testCifrado.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void putNonExistingCifrado() throws Exception {
        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();
        cifrado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCifradoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cifrado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cifrado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCifrado() throws Exception {
        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();
        cifrado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCifradoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cifrado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCifrado() throws Exception {
        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();
        cifrado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCifradoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cifrado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCifradoWithPatch() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();

        // Update the cifrado using partial update
        Cifrado partialUpdatedCifrado = new Cifrado();
        partialUpdatedCifrado.setId(cifrado.getId());

        partialUpdatedCifrado.nombre(UPDATED_NOMBRE);

        restCifradoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCifrado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCifrado))
            )
            .andExpect(status().isOk());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
        Cifrado testCifrado = cifradoList.get(cifradoList.size() - 1);
        assertThat(testCifrado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCifrado.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCifrado.getCifrado()).isEqualTo(DEFAULT_CIFRADO);
        assertThat(testCifrado.getFechaVencimiento()).isEqualTo(DEFAULT_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void fullUpdateCifradoWithPatch() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();

        // Update the cifrado using partial update
        Cifrado partialUpdatedCifrado = new Cifrado();
        partialUpdatedCifrado.setId(cifrado.getId());

        partialUpdatedCifrado
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .cifrado(UPDATED_CIFRADO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO);

        restCifradoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCifrado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCifrado))
            )
            .andExpect(status().isOk());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
        Cifrado testCifrado = cifradoList.get(cifradoList.size() - 1);
        assertThat(testCifrado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCifrado.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCifrado.getCifrado()).isEqualTo(UPDATED_CIFRADO);
        assertThat(testCifrado.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
    }

    @Test
    @Transactional
    void patchNonExistingCifrado() throws Exception {
        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();
        cifrado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCifradoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cifrado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cifrado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCifrado() throws Exception {
        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();
        cifrado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCifradoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cifrado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCifrado() throws Exception {
        int databaseSizeBeforeUpdate = cifradoRepository.findAll().size();
        cifrado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCifradoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cifrado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cifrado in the database
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCifrado() throws Exception {
        // Initialize the database
        cifradoRepository.saveAndFlush(cifrado);

        int databaseSizeBeforeDelete = cifradoRepository.findAll().size();

        // Delete the cifrado
        restCifradoMockMvc
            .perform(delete(ENTITY_API_URL_ID, cifrado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cifrado> cifradoList = cifradoRepository.findAll();
        assertThat(cifradoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
