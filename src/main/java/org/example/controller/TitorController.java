package org.example.controller;

import org.example.model.Titor;
import org.example.service.TitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/titores")
public class TitorController {

    private final TitorService titorService;

    public TitorController(TitorService titorService) {
        this.titorService = titorService;
    }

    // Crear titor
    @PostMapping
    public ResponseEntity<Titor> crearTitor(@RequestBody Titor titor) {
        return ResponseEntity.ok(titorService.crearOuActualizarTitor(titor));
    }

    // Modificar titor
    @PutMapping("/{id}")
    public ResponseEntity<Titor> modificarTitor(@PathVariable Long id, @RequestBody Titor titor) {
        return titorService.obterPorId(id)
                .map(t -> {
                    t.setNome(titor.getNome());
                    t.setApelidos(titor.getApelidos());
                    return ResponseEntity.ok(titorService.crearOuActualizarTitor(t));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Obter titor + alumnos
    @GetMapping("/{id}")
    public ResponseEntity<Titor> obterTitor(@PathVariable Long id) {
        return titorService.obterPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obter todos
    @GetMapping
    public List<Titor> obterTodos() {
        return titorService.obterTodosTitores();
    }

    // Eliminar titor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTitor(@PathVariable Long id) {
        if (titorService.obterPorId(id).isPresent()) {
            titorService.eliminarTitor(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
