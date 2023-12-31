package com.example.dev_web_gerador.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String titulo;

    private String descricao;

    @ManyToOne
    private TipoTarefa tipoTarefa;

    @ManyToOne
    private HistoriaUsuario historiaUsuario;


    @ManyToOne
    @Nullable
    private Tarefa tarefaPai;

    @OneToMany(mappedBy = "tarefaPai")
    @JsonIgnore
    private List<Tarefa> tarefaDependente;


}
