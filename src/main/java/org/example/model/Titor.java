package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titor")
public class Titor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_titor;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "apelidos", nullable = false, length = 150)
    private String apelidos;

    @OneToMany(mappedBy = "titor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Alumno> alumnos;

    public Titor() {
    }

    public Titor(String nome, String apelidos) {
        this.nome = nome;
        this.apelidos = apelidos;
    }

    public Long getId_titor() {
        return id_titor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelidos() {
        return apelidos;
    }

    public void setApelidos(String apelidos) {
        this.apelidos = apelidos;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }
}