package com.desiato.tuningsystems.services;

import com.desiato.tuningsystems.models.Scale;
import com.desiato.tuningsystems.repositories.ScaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScaleService {

    @Autowired
    private ScaleRepository scaleRepository;

    public List<Scale> getAllScales() {
        return scaleRepository.findAll();
    }

    public Optional<Scale> getScaleById(Long id) {
        return scaleRepository.findById(id);
    }

    public Scale saveScale(Scale scale) {
        return scaleRepository.save(scale);
    }

    public void deleteScale(Long id) {
        scaleRepository.deleteById(id);
    }
}
