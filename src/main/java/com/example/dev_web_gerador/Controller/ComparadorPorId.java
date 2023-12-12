package com.example.dev_web_gerador.Controller;
import com.example.dev_web_gerador.Model.Epico;

import java.util.Comparator;

public class ComparadorPorId implements Comparator<Epico> {

    @Override
    public int compare(Epico epico1, Epico epico2) {
        // Assumindo que o ID dos Epicos é do tipo Long
        return Long.compare(epico1.getId(), epico2.getId());
    }
}

