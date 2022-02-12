package dw.trabalho1.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dw.trabalho1.model.Jogador;
import java.util.Optional;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {
    List<Jogador> findByNomeContaining(String nome);

    Optional<Jogador> findById(long cod_jogador);}