package edu.debom.tarefas.controller;

import edu.debom.tarefas.entity.Tarefa;
import edu.debom.tarefas.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    
    @Autowired
    private TarefaRepository tarefaRepository;
    
    // GET - Listar todas as tarefas
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        try {
            List<Tarefa> tarefas = tarefaRepository.findAll();
            return new ResponseEntity<>(tarefas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        
        if (tarefa.isPresent()) {
            return new ResponseEntity<>(tarefa.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // POST - Criar nova tarefa
    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        try {
            if (tarefa.getNome() == null || tarefa.getNome().trim().isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            if (tarefa.getResponsavel() == null || tarefa.getResponsavel().trim().isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            if (tarefa.getDataEntrega() == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            
            Tarefa novaTarefa = tarefaRepository.save(tarefa);
            return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // PUT - Atualizar tarefa
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        Optional<Tarefa> tarefaExistente = tarefaRepository.findById(id);
        
        if (tarefaExistente.isPresent()) {
            try {
                Tarefa tarefa = tarefaExistente.get();
                
                if (tarefaAtualizada.getNome() != null && !tarefaAtualizada.getNome().trim().isEmpty()) {
                    tarefa.setNome(tarefaAtualizada.getNome());
                }
                if (tarefaAtualizada.getDataEntrega() != null) {
                    tarefa.setDataEntrega(tarefaAtualizada.getDataEntrega());
                }
                if (tarefaAtualizada.getResponsavel() != null && !tarefaAtualizada.getResponsavel().trim().isEmpty()) {
                    tarefa.setResponsavel(tarefaAtualizada.getResponsavel());
                }
                
                Tarefa tarefaSalva = tarefaRepository.save(tarefa);
                return new ResponseEntity<>(tarefaSalva, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // DELETE - Deletar tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletarTarefa(@PathVariable Long id) {
        try {
            Optional<Tarefa> tarefa = tarefaRepository.findById(id);
            
            if (tarefa.isPresent()) {
                tarefaRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}