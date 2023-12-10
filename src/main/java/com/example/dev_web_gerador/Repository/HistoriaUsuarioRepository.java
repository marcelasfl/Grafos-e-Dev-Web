package com.example.dev_web_gerador.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dev_web_gerador.Model.HistoriaUsuario;

//import java.util.List;

@Repository
public interface HistoriaUsuarioRepository extends JpaRepository<HistoriaUsuario, Long> {
    // List<TipoHistoriaUsuario> findAllByTipo_epico(Long)

    @Query("SELECT hu FROM HistoriaUsuario hu WHERE hu.historiaUsuarioPai.id = :historiaUsuarioId")
    List<HistoriaUsuario> findByDepedentes(@Param("historiaUsuarioId") Long historiaUsuarioId);

}
