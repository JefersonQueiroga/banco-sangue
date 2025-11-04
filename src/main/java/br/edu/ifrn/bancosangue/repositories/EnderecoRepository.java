package br.edu.ifrn.bancosangue.repositories;

import br.edu.ifrn.bancosangue.domain.entities.EnderecoDoador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<EnderecoDoador,Long> {

    List<EnderecoDoador> findByDoadorId(Long doadorId);
}
