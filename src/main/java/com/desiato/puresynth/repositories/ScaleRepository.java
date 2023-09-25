package com.desiato.puresynth.repositories;

import com.desiato.puresynth.models.Scale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScaleRepository extends JpaRepository<Scale, Long> {

}