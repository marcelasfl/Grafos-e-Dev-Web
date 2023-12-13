package com.example.dev_web_gerador;

import java.util.Comparator;

//import lib.ArvoreBinariaExemplo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.dev_web_gerador.DTO.EpicoDTO;
import com.example.dev_web_gerador.lib.ArvoreBinariaExemplo;

@Configuration
public class AppConfig {

    @Bean
    public ArvoreBinariaExemplo<EpicoDTO> arvoreBinaria() {
        return new ArvoreBinariaExemplo<>(Comparator.comparing(EpicoDTO::getId));
    }
}
