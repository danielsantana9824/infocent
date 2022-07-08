package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cifrado;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cifrado}.
 */
public interface CifradoService {
    /**
     * Save a cifrado.
     *
     * @param cifrado the entity to save.
     * @return the persisted entity.
     */
    Cifrado save(Cifrado cifrado);

    /**
     * Partially updates a cifrado.
     *
     * @param cifrado the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cifrado> partialUpdate(Cifrado cifrado);

    /**
     * Get all the cifrados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cifrado> findAll(Pageable pageable);

    /**
     * Get the "id" cifrado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cifrado> findOne(Long id);

    /**
     * Delete the "id" cifrado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
