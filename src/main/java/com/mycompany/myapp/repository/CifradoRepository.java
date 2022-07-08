package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cifrado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cifrado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CifradoRepository extends JpaRepository<Cifrado, Long>, JpaSpecificationExecutor<Cifrado> {}
