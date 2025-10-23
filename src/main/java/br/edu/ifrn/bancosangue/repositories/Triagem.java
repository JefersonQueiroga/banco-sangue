package br.edu.ifrn.bancosangue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Triagem extends JpaRepository<Triagem, Long> {
}
