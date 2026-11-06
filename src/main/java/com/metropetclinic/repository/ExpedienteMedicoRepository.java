package com.metropetclinic.repository;

import com.metropetclinic.domain.ExpedienteMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpedienteMedicoRepository extends JpaRepository<ExpedienteMedico, Integer> {

}