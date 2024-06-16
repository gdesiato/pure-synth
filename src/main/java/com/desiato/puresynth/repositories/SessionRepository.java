package com.desiato.puresynth.repositories;

import com.desiato.puresynth.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    void deleteByUserId(Long userId);
    Optional<Session> findByToken(String token);


}
