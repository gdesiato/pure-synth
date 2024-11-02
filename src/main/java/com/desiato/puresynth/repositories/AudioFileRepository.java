package com.desiato.puresynth.repositories;

import com.desiato.puresynth.models.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
    List<AudioFile> findAllByUser_Id(Long userId);
}
