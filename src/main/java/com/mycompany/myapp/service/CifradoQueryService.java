package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Cifrado;
import com.mycompany.myapp.repository.CifradoRepository;
import com.mycompany.myapp.service.criteria.CifradoCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cifrado} entities in the database.
 * The main input is a {@link CifradoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cifrado} or a {@link Page} of {@link Cifrado} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CifradoQueryService extends QueryService<Cifrado> {

    private final Logger log = LoggerFactory.getLogger(CifradoQueryService.class);

    private final CifradoRepository cifradoRepository;

    public CifradoQueryService(CifradoRepository cifradoRepository) {
        this.cifradoRepository = cifradoRepository;
    }

    /**
     * Return a {@link List} of {@link Cifrado} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cifrado> findByCriteria(CifradoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cifrado> specification = createSpecification(criteria);
        return cifradoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cifrado} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cifrado> findByCriteria(CifradoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cifrado> specification = createSpecification(criteria);
        return cifradoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CifradoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cifrado> specification = createSpecification(criteria);
        return cifradoRepository.count(specification);
    }

    /**
     * Function to convert {@link CifradoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cifrado> createSpecification(CifradoCriteria criteria) {
        Specification<Cifrado> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cifrado_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Cifrado_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Cifrado_.descripcion));
            }
            if (criteria.getCifrado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCifrado(), Cifrado_.cifrado));
            }
            if (criteria.getFechaVencimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaVencimiento(), Cifrado_.fechaVencimiento));
            }
        }
        return specification;
    }
}
