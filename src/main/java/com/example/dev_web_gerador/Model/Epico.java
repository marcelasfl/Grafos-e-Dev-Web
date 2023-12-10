package com.example.dev_web_gerador.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class Epico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private String relevancia;

    private String categoria;

    @OneToMany(mappedBy = "epico", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<HistoriaUsuario> historiaUsuario;

    @ManyToOne
    private TipoEpico tipoEpico;

    @ManyToOne
    @Nullable
    private Projeto projeto;

    @ManyToOne
    @Nullable
    private Epico epicoPai;

    @OneToMany(mappedBy = "epicoPai")
    @JsonIgnore
    private List<Epico> epicoDependente;

    // @ManyToMany(mappedBy = "epicoDependente")
    // @JsonIgnoreProperties("epicoDependente")
    // private List<Epico> epicoDependente;

}
