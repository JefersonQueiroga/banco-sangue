package br.edu.ifrn.bancosangue.services;

import br.edu.ifrn.bancosangue.domain.entities.Doador;
import br.edu.ifrn.bancosangue.exceptions.BusinessException;
import br.edu.ifrn.bancosangue.repositories.DoadorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class DoadorService {

    private final DoadorRepository doadorRepository;

    public DoadorService(DoadorRepository doadorRepository) {

        this.doadorRepository = doadorRepository;
    }

    // Método para criar um novo doador
    public Doador criarDoador(Doador doador) {
        validarRegraNegocioParaCriacao(doador);
        return doadorRepository.save(doador);
    }

    // Método para listar todos os doadores
    public List<Doador> listaDoadores() {
        return doadorRepository.findAll();
    }

    // Método para atualizar um doador existente
    public Doador atualizarDoador(Long id, Doador dadosAtualizados) {
        Doador doadorExistente = obterDoadorPorId(id);

        if ( dadosAtualizados.getNomeCompleto() != null) {
            doadorExistente.setNomeCompleto(dadosAtualizados.getNomeCompleto());
        }
        if ( dadosAtualizados.getDataNascimento() != null) {
            validarIdade(dadosAtualizados.getDataNascimento());
            doadorExistente.setDataNascimento(dadosAtualizados.getDataNascimento());
        }
        if(dadosAtualizados.getCpf() != null ) {
            //antes precisamos ver se nao existe outro doador com esse cpf
            var DoadorComMesmoCpf = doadorRepository.findByCpf(dadosAtualizados.getCpf());
            if (DoadorComMesmoCpf != null && !DoadorComMesmoCpf.getId().equals(id)) {
                throw new BusinessException("CPF já cadastrado para outro doador.");
            }
            doadorExistente.setCpf(dadosAtualizados.getCpf());
        }

        //atualizar os resto dos campos conforme necessário
        doadorExistente.setEmail(dadosAtualizados.getEmail());
        doadorExistente.setTelefone(dadosAtualizados.getTelefone());

        return doadorRepository.save(doadorExistente);
    }

    // Método para obter um doador por ID
    public Doador obterDoadorPorId(Long id) {
        return doadorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Doador não encontrado com o ID: " + id));
    }

    //Método para delatar um doador
    public void deletarDoador(Long id) {
        Doador doadorExistente = obterDoadorPorId(id);
        doadorRepository.delete(doadorExistente);
    }



    private void validarRegraNegocioParaCriacao(Doador r) {
        validarIdade(r.getDataNascimento());
        if (doadorRepository.existsByCpf(r.getCpf())) {
            throw new BusinessException("CPF já cadastrado.");
        }
    }

    private void validarIdade(LocalDate dataNascimento) {
        if (dataNascimento == null) throw new BusinessException("Data de nascimento é obrigatória.");
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
        if (idade < 16 || idade > 69) {
            throw new BusinessException("Idade inválida para doação (permitido entre 16 e 69 anos).");
        }
    }

}
