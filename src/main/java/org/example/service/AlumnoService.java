package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Alumno;
import org.example.model.Titor;
import org.example.repository.AlumnoRepository;
import org.example.repository.TitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private TitorRepository titorRepository;

    @Transactional
    public Alumno crearOuActualizarAlumno (Alumno alumno) {
        Long id_Titor = alumno.getTitor().getId_titor();
        Titor titor = titorRepository.findById(id_Titor).orElseThrow();
        alumno.setTitor(titor);
        return alumnoRepository.save(alumno);
    }

    public Optional<Alumno> obterPorId(Long id) {
        return alumnoRepository.findById(id);
    }

    public void eliminarAlumno(Long id) {
        alumnoRepository.deleteById(id);
    }
}
