package br.edu.ifrn.bancosangue.repositories;

import br.edu.ifrn.bancosangue.domain.entities.Doador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoadorRepository extends JpaRepository<Doador, Long> {

    boolean existsByCpf(String cpf);
    Doador findByCpf(String cpf);
}
