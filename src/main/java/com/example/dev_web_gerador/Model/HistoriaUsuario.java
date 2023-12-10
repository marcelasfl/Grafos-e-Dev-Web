package com.example.dev_web_gerador.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class HistoriaUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private String relevancia;

    private String categoria;

    @ManyToOne
    private Epico epico;

    @ManyToOne
    private TipoHistoriaUsuario tipoHistoriaUsuario;

    @OneToMany(mappedBy = "historiaUsuario")
    @JsonIgnore
    private List<Tarefa> tarefa;


    @ManyToOne
    @Nullable
    private HistoriaUsuario historiaUsuarioPai;

    @OneToMany(mappedBy = "historiaUsuarioPai")
    @JsonIgnore
    private List<HistoriaUsuario> historiaUsuarioDependente;



}
