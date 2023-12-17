package com.example.dev_web_gerador.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.dev_web_gerador.Model.*;
import com.example.dev_web_gerador.Repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev_web_gerador.DTO.EpicoDTO;
import com.example.dev_web_gerador.DTO.EpicoInputDTO;
import com.example.dev_web_gerador.codes.StatusCode;
import com.example.dev_web_gerador.lib.ArvoreBinariaExemplo;

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
    HistoriaUsuarioRepository historiaUsuarioRepository;

    @Autowired
    TipoHistoriaUsuarioRepository tipoHistoriaUsuarioRepository;

    @Autowired
    ArvoreBinariaExemplo<EpicoDTO> arvoreBinaria;

    @PostMapping("/criarEpico")
    public ResponseEntity<Epico> criarEpico(@RequestBody EpicoInputDTO epicoInputDTO) {
        var epico = new Epico();
        Long tipoEpicoId = epicoInputDTO.tipoEpico_id();

        Optional<TipoEpico> tipoEpicoOptional = tipoEpicoRepository.findById(tipoEpicoId);
        Optional<Epico> epicoOptional = epicoRepository.findById(epicoInputDTO.epicoPai_id());
        Optional<Projeto> projetoId = projetoRepository.findById(epicoInputDTO.projeto_id());

        if (epicoOptional.isPresent()) {
            epico.setEpicoPai(epicoOptional.get());
        }

        if (projetoId.isPresent()) {
            epico.setProjeto(projetoId.get());
        }

        epico.setTipoEpico(tipoEpicoOptional.get());

        BeanUtils.copyProperties(epicoInputDTO, epico);

        Epico epicoSalvo = epicoRepository.save(epico);

        gerarHistoriaUsuario(epicoSalvo.getId());

        Epico epicoArvore = epicoRepository.findById(epicoSalvo.getId()).get();
        EpicoDTO epicoc = new EpicoDTO();
        epicoc.setId(epicoArvore.getId());
        epicoc.setTitulo(epicoArvore.getTitulo());
        epicoc.setDescricao(epicoArvore.getDescricao());
        epicoc.setRelevancia(epicoArvore.getRelevancia());
        epicoc.setCategoria(epicoArvore.getCategoria());

        arvoreBinaria.adicionar(epicoc);

        return ResponseEntity.status(HttpStatus.CREATED).body(epico);
    }

    @PostMapping("/adicionarNaArvore")
    public ResponseEntity<String> adicionarEpicoNaArvore(EpicoDTO epico) { // Adicionando os epicos na arvore

        arvoreBinaria.adicionar(epico);

        return ResponseEntity.status(HttpStatus.CREATED).body("Épico adicionado à árvore com sucesso.");
    }


    public ResponseEntity<List<HistoriaUsuario>> gerarHistoriaUsuario(@PathVariable(value = "epico_id") long epico_id) {
        Epico epico = epicoRepository.findById(epico_id).get(); //Prourando o epico pelo id passado
        Long tipoEpicoId = epico.getTipoEpico().getId(); //Armazenando o id


        List<TipoHistoriaUsuario> tipoHistoriaUsuario = tipoHistoriaUsuarioRepository.findByTipoEpicoId(tipoEpicoId);

        String epicoDescricao = epico.getDescricao(); //Armazenando a desccrição do épico para ser modificada e adicionada no hu
        List<HistoriaUsuario> historias = new ArrayList<HistoriaUsuario>();
        tipoHistoriaUsuario.forEach(tipo -> { //Pegando ccada historia retornada na lista
            String palavra = epicoDescricao.replaceAll("(?<=\\bdesejo\\s)\\w+", tipo.getDescricao()); //Substituindo a palavra desejo pelo verbo

            HistoriaUsuario historiaUsuario = salvarHistoriaUsuario(epico.getId(), epico, palavra, tipo.getId());

            historias.add(historiaUsuario);

        });

        return ResponseEntity.ok(historias);
    }


    private HistoriaUsuario salvarHistoriaUsuario(long epico_id,Epico epico, String descricao, long tipoHistoriaUsuario) {
        HistoriaUsuario historiaUsuario = new HistoriaUsuario();

        Optional<Epico> epicoOptional = epicoRepository.findById(epico.getId());
        Optional<TipoHistoriaUsuario> tipoHistoriaUsuarioOptional = tipoHistoriaUsuarioRepository.findById(tipoHistoriaUsuario);
        historiaUsuario.setCategoria(epico.getCategoria());
        historiaUsuario.setDescricao(descricao);
        historiaUsuario.setRelevancia(epico.getRelevancia());
        historiaUsuario.setTitulo(epico.getTitulo());
        historiaUsuario.setEpico(epicoOptional.get());
        historiaUsuario.setTipoHistoriaUsuario(tipoHistoriaUsuarioOptional.get());

        return historiaUsuarioRepository.save(historiaUsuario);
    }

    @DeleteMapping("/removerDaArvore/{id}")
    public ResponseEntity<Object> removerEpicoDaArvore(@PathVariable long id) { // Remover o epico da arvore
        EpicoDTO epicoRemovido = arvoreBinaria.removerPorId(id);

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
        Optional<Epico> epico = epicoRepository.findById(id);

        return epico.<ResponseEntity<Object>>map(epicos ->
                ResponseEntity.status(HttpStatus.OK).body(epicos)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode()));
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

            if (epicoPai.isPresent()) {
                epico.setEpicoPai(epicoPai.get());
            }

            if (projetoId.isPresent()) {
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
        //EpicoDTO epicoRemovido = arvoreBinaria.removerPorId(id);
        arvoreBinaria.removerPorId(id);
        if (epico.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode());
        }

        epicoRepository.delete(epico.get());
        return ResponseEntity.status(HttpStatus.OK).body(StatusCode.EPIC_REMOVED.getCode());
    }

    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<Object> pesquisarEpicoNaArvore(@PathVariable long id) { // Pesquisando os nos da arvore pelo
                                                                                  // id
        EpicoDTO epicoEncontrado = arvoreBinaria.pesquisarPorId(id);

        if (epicoEncontrado != null) {
            return ResponseEntity.status(HttpStatus.OK).body(epicoEncontrado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusCode.EPIC_NOT_FOUND.getCode());
        }
    }

    @GetMapping("/caminharEmNivel") // Testando o resultado da árvore com o caminhar em nivel
    public ResponseEntity<Object> caminharEmNivel() {
        String arvore = arvoreBinaria.caminharEmOrdem();

        return ResponseEntity.status(HttpStatus.OK).body(arvore);
    }
}
