package com.example.dev_web_gerador;

import com.example.dev_web_gerador.Model.Epico;
import com.example.dev_web_gerador.lib.ArvoreBinariaExemplo;
//import lib.ArvoreBinariaExemplo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;


@Configuration
public class AppConfig {

    @Bean
    public ArvoreBinariaExemplo<Epico> arvoreBinaria() {
        return new ArvoreBinariaExemplo<>(Comparator.comparing(Epico::getId));
    }
}

