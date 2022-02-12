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
import dw.trabalho1.model.Pagamento;
import dw.trabalho1.repository.JogadorRepository;
import dw.trabalho1.repository.PagamentoRepository;

@RestController
@RequestMapping("/")
public class PagamentoController {

    @Autowired
    private PagamentoRepository metodosPagamento;

    @Autowired
    private JogadorRepository metodosJogador;

    
    @GetMapping("pagamentos")
    public ResponseEntity<List<Pagamento>> listagem(@RequestParam(required = false) String ano, @RequestParam(required = false) String mes) {
        try {
            List<Pagamento> pagamentos = new ArrayList<Pagamento>();
            this.metodosPagamento.findAll().forEach(pagamentos::add);

            if (pagamentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(pagamentos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @DeleteMapping("pagamentos")
    public ResponseEntity<HttpStatus> deleteAll() {
        try {
            this.metodosPagamento.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("pagamentos/{id}")
    public ResponseEntity<Pagamento> mostrar(@PathVariable("id") long id) {
        Optional<Pagamento> data = this.metodosPagamento.findById(id);

        if (data.isPresent())
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("pagamentos/{id}")
    public ResponseEntity<Pagamento> editar(@PathVariable("id") long id, @RequestBody Pagamento novoPagamento) {

        Optional<Pagamento> data = this.metodosPagamento.findById(id);

        if (data.isPresent()) {
           Pagamento pagamento = data.get();
           pagamento.setAno(novoPagamento.getAno());
           pagamento.setMes(novoPagamento.getMes());
           pagamento.setValor(novoPagamento.getValor());

           return new ResponseEntity<>(this.metodosPagamento.save(pagamento), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("jogadores/{jogadorId}/pagamentos")
    public ResponseEntity<HttpStatus> deletarTudoJogador(@PathVariable("jogadorId") long jogadorId) {
        try {
            Optional<Jogador> dadosJogador = this.metodosJogador.findById(jogadorId);
            if (dadosJogador.isPresent()) {
                Jogador jogador = dadosJogador.get();
                List<Pagamento> pagamentos = jogador.getPagamentos();
                this.metodosPagamento.deleteAll(pagamentos);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("pagamentos/{id}")
    public ResponseEntity<HttpStatus> deletar(@PathVariable("id") long id) {
        try {
            this.metodosPagamento.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
   
    @PostMapping("jogadores/{jogadorId}/pagamentos")
    public ResponseEntity<Pagamento> criacao(@PathVariable("jogadorId") long jogadorId, @RequestBody Pagamento pagamento) {
        try {
            Optional<Pagamento> novoPagamento = this.metodosJogador.findById(jogadorId).map(jogador -> {
                pagamento.setJogador(jogador);
                return this.metodosPagamento.save(pagamento);
            });

            if (novoPagamento.isPresent()) {
                return new ResponseEntity<>(novoPagamento.get(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }            
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   
    @GetMapping("jogadores/{jogadorId}/pagamentos")
    public ResponseEntity<List<Pagamento>> mostrarPagJogador(@PathVariable("jogadorId") long jogadorId) {
        Optional<Jogador> jogador = this.metodosJogador.findById(jogadorId);

        if (jogador.isPresent()){
            List<Pagamento> pagamentos = jogador.get().getPagamentos();

            if (pagamentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(pagamentos, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    

}