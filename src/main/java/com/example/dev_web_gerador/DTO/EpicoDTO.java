package com.example.dev_web_gerador.DTO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "epicoPai")
public class EpicoDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String relevancia;
    private String categoria;
    private EpicoDTO epicoPai;
}