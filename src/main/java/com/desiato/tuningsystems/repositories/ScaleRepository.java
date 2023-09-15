package com.desiato.tuningsystems.repositories;

import com.desiato.tuningsystems.models.Scale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScaleRepository extends JpaRepository<Scale, Long> {

}