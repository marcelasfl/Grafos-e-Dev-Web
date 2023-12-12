package com.example.dev_web_gerador.Controller;

import com.example.dev_web_gerador.DTO.EpicoInputDTO;
import com.example.dev_web_gerador.Model.Epico;
import com.example.dev_web_gerador.Model.Projeto;
import com.example.dev_web_gerador.Model.TipoEpico;
import com.example.dev_web_gerador.Repository.EpicoRepository;
import com.example.dev_web_gerador.Repository.ProjetoRepository;
import com.example.dev_web_gerador.Repository.TipoEpicoRepository;
import com.example.dev_web_gerador.codes.StatusCode;
import com.example.dev_web_gerador.lib.ArvoreBinariaExemplo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/epico")
public class EpicoController {

    @Autowired
    TipoEpicoRepository tipoEpicoRepository;

    @Autowired
    EpicoRepository epicoRepository;

    @Autowired
    ProjetoRepository projetoRepository;

    @Autowired
    ArvoreBinariaExemplo<Epico> arvoreBinaria;

    @PostMapping
    public ResponseEntity<Epico> criarEpico(@RequestBody EpicoInputDTO epicoInputDTO) {
        var epico = new Epico();
        Long tipoEpicoId = epicoInputDTO.tipoEpico_id();

        Optional<TipoEpico> tipoEpicoOptional = tipoEpicoRepository.findById(tipoEpicoId);
        Optional<Epico> epicoOptional = epicoRepository.findById(epicoInputDTO.epicoPai_id());
        Optional<Projeto> projetoId = projetoRepository.findById(epicoInputDTO.projeto_id());

        if(epicoOptional.isPresent()){
            epico.setEpicoPai(epicoOptional.get());
        }

        if(projetoId.isPresent()){
            epico.setProjeto(projetoId.get());
        }


        epico.setTipoEpico(tipoEpicoOptional.get());

        BeanUtils.copyProperties(epicoInputDTO, epico);

       Epico epicoSalvo = epicoRepository.save(epico);

       Epico epicoArvore = epicoRepository.findById(epicoSalvo.getId()).get();

        arvoreBinaria.adicionar(epicoArvore);

        return ResponseEntity.status(HttpStatus.CREATED).body(epico);
    }

    @PostMapping("/adicionarNaArvore")
    public ResponseEntity<String> adicionarEpicoNaArvore(Epico epico) { //Adicionando os epicos na arvore


        arvoreBinaria.adicionar(epico);

        return ResponseEntity.status(HttpStatus.CREATED).body("Épico adicionado à árvore com sucesso.");
    }

    @DeleteMapping("/removerDaArvore/{id}")
    public ResponseEntity<Object> removerEpicoDaArvore(@PathVariable long id) { //Remover o epico da arvore
        Epico epicoRemovido = arvoreBinaria.removerPorId(id);

        if (epicoRemovido != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Épico removido da árvore com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode());
        }
    }


    @GetMapping
    public ResponseEntity<List<Epico>> listarEpico() {
        return ResponseEntity.status(HttpStatus.OK).body(epicoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> epicoId(@PathVariable long id) {
        Epico epicoEncontrado = arvoreBinaria.pesquisarPorId(id);

        if (epicoEncontrado != null) {
            return ResponseEntity.status(HttpStatus.OK).body(epicoEncontrado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Epico> atualizarEpico(
            @PathVariable long id,
            @RequestBody EpicoInputDTO epicoInputDTO) {

        Optional<Epico> epicoOptional = epicoRepository.findById(id);

        if (epicoOptional.isPresent()) {

            Epico epico = epicoOptional.get();

            Long tipoEpicoId = epicoInputDTO.tipoEpico_id();

            Optional<TipoEpico> tipoEpicoOptional = tipoEpicoRepository.findById(tipoEpicoId);
            Optional<Epico> epicoPai = epicoRepository.findById(epicoInputDTO.epicoPai_id());
            Optional<Projeto> projetoId = projetoRepository.findById(epicoInputDTO.projeto_id());

            if (tipoEpicoOptional.isPresent()) {
                epico.setTipoEpico(tipoEpicoOptional.get());

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if(epicoPai.isPresent()){
                epico.setEpicoPai(epicoPai.get());
            }

            if(projetoId.isPresent()){
                epico.setProjeto(projetoId.get());
            }

            BeanUtils.copyProperties(epicoInputDTO, epico);

            // Retorna a resposta com o tipo de história de usuário atualizado
            return ResponseEntity.status(HttpStatus.OK).body(epicoRepository.save(epico));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarEpico(@PathVariable(value = "id") long id) {
        Optional<Epico> epico = epicoRepository.findById(id);
        arvoreBinaria.removerPorId(id);
        if (epico.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode());}

        epicoRepository.delete(epico.get());
        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.EPIC_REMOVED.getCode());
    }


    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<Object> pesquisarEpicoNaArvore(@PathVariable long id) { //Pesquisando os nos da arvore pelo id
        Epico epicoEncontrado = arvoreBinaria.pesquisarPorId(id);

        if (epicoEncontrado != null) {
            return ResponseEntity.status(HttpStatus.OK).body(epicoEncontrado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode());
        }
    }

    @GetMapping("/caminharEmNivel") //Testando o resultado da árvore com o caminhar em nivel
    public ResponseEntity<Object> caminharEmNivel() {
        String arvore = arvoreBinaria.caminharEmOrdem();

        return ResponseEntity.status(HttpStatus.OK).body(arvore);
    }
}




