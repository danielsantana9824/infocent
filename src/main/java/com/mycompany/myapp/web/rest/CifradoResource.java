package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Cifrado;
import com.mycompany.myapp.repository.CifradoRepository;
import com.mycompany.myapp.service.CifradoQueryService;
import com.mycompany.myapp.service.CifradoService;
import com.mycompany.myapp.service.criteria.CifradoCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.Definition.Undefined;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Cifrado}.
 */
@RestController
@RequestMapping("/api")
public class CifradoResource {

    private final Logger log = LoggerFactory.getLogger(CifradoResource.class);

    private static final String ENTITY_NAME = "cifrado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public String cambioDefinitivo;

    private final CifradoService cifradoService;

    private final CifradoRepository cifradoRepository;

    private final CifradoQueryService cifradoQueryService;

    public CifradoResource(CifradoService cifradoService, CifradoRepository cifradoRepository, CifradoQueryService cifradoQueryService) {
        this.cifradoService = cifradoService;
        this.cifradoRepository = cifradoRepository;
        this.cifradoQueryService = cifradoQueryService;
    }

    /**
     * {@code POST  /cifrados} : Create a new cifrado.
     *
     * @param cifrado the cifrado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cifrado, or with status {@code 400 (Bad Request)} if the cifrado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cifrados")
    public ResponseEntity<Cifrado> createCifrado(@Valid @RequestBody Cifrado cifrado) throws URISyntaxException {
        log.debug("REST request to save Cifrado : {}", cifrado);
        if (cifrado.getId() != null) {
            throw new BadRequestAlertException("A new cifrado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        char cambio[] = cifrado.getCifrado().toCharArray();
        for (int i = 0; i < cambio.length; i++) {
            cambio[i] = (char)(cambio[i]+(char)5); 
        }

        this.cambioDefinitivo = String.valueOf(cambio);
        cifrado.cifrado(this.cambioDefinitivo);

        Cifrado result = cifradoService.save(cifrado);
        return ResponseEntity
            .created(new URI("/api/cifrados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cifrados/:id} : Updates an existing cifrado.
     *
     * @param id the id of the cifrado to save.
     * @param cifrado the cifrado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cifrado,
     * or with status {@code 400 (Bad Request)} if the cifrado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cifrado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cifrados/{id}")
    public ResponseEntity<Cifrado> updateCifrado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cifrado cifrado
    ) throws URISyntaxException {
        log.debug("REST request to update Cifrado : {}, {}", id, cifrado);
        if (cifrado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cifrado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cifradoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cifrado result = cifradoService.save(cifrado);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cifrado.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cifrados/:id} : Partial updates given fields of an existing cifrado, field will ignore if it is null
     *
     * @param id the id of the cifrado to save.
     * @param cifrado the cifrado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cifrado,
     * or with status {@code 400 (Bad Request)} if the cifrado is not valid,
     * or with status {@code 404 (Not Found)} if the cifrado is not found,
     * or with status {@code 500 (Internal Server Error)} if the cifrado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cifrados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cifrado> partialUpdateCifrado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cifrado cifrado
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cifrado partially : {}, {}", id, cifrado);
        if (cifrado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cifrado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cifradoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cifrado> result = cifradoService.partialUpdate(cifrado);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cifrado.getId().toString())
        );
    }

    /**
     * {@code GET  /cifrados} : get all the cifrados.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cifrados in body.
     */
    @GetMapping("/cifrados")
    public ResponseEntity<List<Cifrado>> getAllCifrados(
        CifradoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Cifrados by criteria: {}", criteria);
        Page<Cifrado> page = cifradoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cifrados/count} : count all the cifrados.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cifrados/count")
    public ResponseEntity<Long> countCifrados(CifradoCriteria criteria) {
        log.debug("REST request to count Cifrados by criteria: {}", criteria);
        return ResponseEntity.ok().body(cifradoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cifrados/:id} : get the "id" cifrado.
     *
     * @param id the id of the cifrado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cifrado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cifrados/{id}")
    public ResponseEntity<Cifrado> getCifrado(@PathVariable Long id) {
        log.debug("REST request to get Cifrado : {}", id);
        Optional<Cifrado> cifrado = cifradoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cifrado);
    }

    /**
     * {@code DELETE  /cifrados/:id} : delete the "id" cifrado.
     *
     * @param id the id of the cifrado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cifrados/{id}")
    public ResponseEntity<Void> deleteCifrado(@PathVariable Long id) {
        log.debug("REST request to delete Cifrado : {}", id);
        cifradoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
