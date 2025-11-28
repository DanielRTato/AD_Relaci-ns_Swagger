package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Titor;
import org.example.repository.TitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitorService {

    @Autowired
    private TitorRepository titorRepository;

    @Transactional
    public Titor crearOuActualizarTitor (Titor titor) {
        return titorRepository.save(titor);
    }

    public List<Titor> obterTodosTitores() {
        return titorRepository.findAll();
    }

    public Optional<Titor> obterPorId(Long id) {
        return titorRepository.findById(id);
    }

    public void eliminarTitor(Long id) {
        titorRepository.deleteById(id);
    }

}
