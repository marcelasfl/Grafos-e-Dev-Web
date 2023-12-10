package com.example.dev_web_gerador.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dev_web_gerador.Model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    @Query("SELECT ta FROM Tarefa ta WHERE ta.tarefaPai.id = :tarefaId")
    List<Tarefa> findByDepedentes(@Param("tarefaId") Long tarefaId);

}
