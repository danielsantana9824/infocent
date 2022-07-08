package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Cifrado;
import com.mycompany.myapp.repository.CifradoRepository;
import com.mycompany.myapp.service.CifradoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cifrado}.
 */
@Service
@Transactional
public class CifradoServiceImpl implements CifradoService {

    private final Logger log = LoggerFactory.getLogger(CifradoServiceImpl.class);

    private final CifradoRepository cifradoRepository;

    public CifradoServiceImpl(CifradoRepository cifradoRepository) {
        this.cifradoRepository = cifradoRepository;
    }

    @Override
    public Cifrado save(Cifrado cifrado) {
        log.debug("Request to save Cifrado : {}", cifrado);
        return cifradoRepository.save(cifrado);
    }

    @Override
    public Optional<Cifrado> partialUpdate(Cifrado cifrado) {
        log.debug("Request to partially update Cifrado : {}", cifrado);

        return cifradoRepository
            .findById(cifrado.getId())
            .map(existingCifrado -> {
                if (cifrado.getNombre() != null) {
                    existingCifrado.setNombre(cifrado.getNombre());
                }
                if (cifrado.getDescripcion() != null) {
                    existingCifrado.setDescripcion(cifrado.getDescripcion());
                }
                if (cifrado.getCifrado() != null) {
                    existingCifrado.setCifrado(cifrado.getCifrado());
                }
                if (cifrado.getFechaVencimiento() != null) {
                    existingCifrado.setFechaVencimiento(cifrado.getFechaVencimiento());
                }

                return existingCifrado;
            })
            .map(cifradoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cifrado> findAll(Pageable pageable) {
        log.debug("Request to get all Cifrados");
        return cifradoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cifrado> findOne(Long id) {
        log.debug("Request to get Cifrado : {}", id);
        return cifradoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cifrado : {}", id);
        cifradoRepository.deleteById(id);
    }
}
