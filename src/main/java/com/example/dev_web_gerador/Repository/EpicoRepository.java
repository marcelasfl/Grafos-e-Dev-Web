package com.example.dev_web_gerador.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dev_web_gerador.Model.Epico;

@Repository
public interface EpicoRepository extends JpaRepository<Epico, Long> {
    @Query("SELECT ep FROM Epico ep WHERE ep.epicoPai.id = :epicoId")
    List<Epico> findByDepedentes(@Param("epicoId") Long epicoId);
}