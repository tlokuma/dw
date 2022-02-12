package dw.trabalho1.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dw.trabalho1.model.Jogador;
import dw.trabalho1.repository.JogadorRepository;

@RestController
@RequestMapping("/jogadores")
public class JogadorController {
    @Autowired
    private JogadorRepository metodosJogador;

    @GetMapping("")
    public ResponseEntity<List<Jogador>> listagem(@RequestParam(required = false) String nome) {
        try {
            List<Jogador> jogador = new ArrayList<Jogador>();

            if (nome == null)
                this.metodosJogador.findAll().forEach(jogador::add);
            else
                this.metodosJogador.findByNomeContaining(nome).forEach(jogador::add);

            if (jogador.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(jogador, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Jogador> editar(@PathVariable("id") long id, @RequestBody Jogador _Jogador) {

        Optional<Jogador> data = this.metodosJogador.findById(id);

        if (data.isPresent()) {
           Jogador jogador = data.get();
           jogador.setNome(_Jogador.getNome());
           jogador.setDataNasc(_Jogador.getDataNasc());
           jogador.setEmail(_Jogador.getEmail());

           return new ResponseEntity<>(this.metodosJogador.save(jogador), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
  

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteAll()
    {
        try {
            this.metodosJogador.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletar(@PathVariable("id") long id) {
        try {
            this.metodosJogador.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Jogador> criacao(@RequestBody Jogador jogador) {
        try {
            Jogador _Jogador = this.metodosJogador.save(new Jogador(jogador.getNome(), jogador.getEmail(), jogador.getDataNasc()));
            
            return new ResponseEntity<>(_Jogador, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogador> mostrar(@PathVariable long id) {
        Optional<Jogador> data = this.metodosJogador.findById(id);

        if (data.isPresent())
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

   
   
}