// src/main/java/br/edu/ifrn/bancosangue/services/EnderecoService.java
package br.edu.ifrn.bancosangue.services;

import br.edu.ifrn.bancosangue.domain.entities.Doador;
import br.edu.ifrn.bancosangue.domain.entities.EnderecoDoador;
import br.edu.ifrn.bancosangue.exceptions.BusinessException;
import br.edu.ifrn.bancosangue.mappers.EnderecoDoadorMapper;
import br.edu.ifrn.bancosangue.repositories.DoadorRepository;
import br.edu.ifrn.bancosangue.repositories.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ifrn.bancosangue.dtos.EnderecoDoadorDTO;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final DoadorRepository doadorRepository;
    private final EnderecoDoadorMapper enderecoMapper;

    @Transactional
    public EnderecoDoadorDTO criarEndereco(Long doadorId, EnderecoDoadorDTO dto) {
        Doador doador = doadorRepository.findById(doadorId)
                .orElseThrow(() -> new BusinessException("Doador não encontrado com ID: " + doadorId));

        EnderecoDoador endereco = enderecoMapper.toEntity(dto);
        endereco.setDoador(doador);
        endereco.setCriadoEm(LocalDateTime.now());
        endereco.setAtualizadoEm(LocalDateTime.now());

        EnderecoDoador enderecoSalvo = enderecoRepository.save(endereco);
        return enderecoMapper.toDTO(enderecoSalvo);
    }

    @Transactional
    public EnderecoDoadorDTO atualizarEndereco(Long doadorId, Long enderecoId, EnderecoDoadorDTO dto) {
        EnderecoDoador enderecoExistente = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new BusinessException("Endereço não encontrado com ID: " + enderecoId));

        // Validar se o endereço pertence ao doador
        if (!enderecoExistente.getDoador().getId().equals(doadorId)) {
            throw new BusinessException("Endereço não pertence ao doador informado");
        }

        // Atualiza todos os campos
        enderecoMapper.atualizarFromDTO(dto, enderecoExistente);
        enderecoExistente.setAtualizadoEm(LocalDateTime.now());

        EnderecoDoador enderecoAtualizado = enderecoRepository.save(enderecoExistente);
        return enderecoMapper.toDTO(enderecoAtualizado);
    }

    @Transactional
    public void removerEndereco(Long doadorId, Long enderecoId) {
        EnderecoDoador endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new BusinessException("Endereço não encontrado com ID: " + enderecoId));

        if (!endereco.getDoador().getId().equals(doadorId)) {
            throw new BusinessException("Endereço não pertence ao doador informado");
        }

        enderecoRepository.delete(endereco);
    }

    @Transactional(readOnly = true)
    public List<EnderecoDoadorDTO> listarEnderecosPorDoador(Long doadorId) {
        if (!doadorRepository.existsById(doadorId)) {
            throw new BusinessException("Doador não encontrado com ID: " + doadorId);
        }

        List<EnderecoDoador> enderecos = enderecoRepository.findByDoadorId(doadorId);
        return enderecoMapper.toDTOList(enderecos);
    }
}