package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Empresa;
import com.mycompany.myapp.repository.EmpresaRepository;
import com.mycompany.myapp.service.EmpresaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Empresa}.
 */
@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Empresa save(Empresa empresa) {
        log.debug("Request to save Empresa : {}", empresa);
        return empresaRepository.save(empresa);
    }

    @Override
    public Optional<Empresa> partialUpdate(Empresa empresa) {
        log.debug("Request to partially update Empresa : {}", empresa);

        return empresaRepository
            .findById(empresa.getId())
            .map(existingEmpresa -> {
                if (empresa.getNombre() != null) {
                    existingEmpresa.setNombre(empresa.getNombre());
                }
                if (empresa.getRif() != null) {
                    existingEmpresa.setRif(empresa.getRif());
                }
                if (empresa.getDireccion() != null) {
                    existingEmpresa.setDireccion(empresa.getDireccion());
                }
                if (empresa.getTelefono() != null) {
                    existingEmpresa.setTelefono(empresa.getTelefono());
                }
                if (empresa.getEmail() != null) {
                    existingEmpresa.setEmail(empresa.getEmail());
                }

                return existingEmpresa;
            })
            .map(empresaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empresa> findAll(Pageable pageable) {
        log.debug("Request to get all Empresas");
        return empresaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empresa> findOne(Long id) {
        log.debug("Request to get Empresa : {}", id);
        return empresaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Empresa : {}", id);
        empresaRepository.deleteById(id);
    }
}
