// src/main/java/br/edu/ifrn/bancosangue/mappers/EnderecoDoadorMapper.java
package br.edu.ifrn.bancosangue.mappers;

import br.edu.ifrn.bancosangue.domain.entities.EnderecoDoador;
import br.edu.ifrn.bancosangue.dtos.EnderecoDoadorDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnderecoDoadorMapper {

    EnderecoDoadorDTO toDTO(EnderecoDoador endereco);
    List<EnderecoDoadorDTO> toDTOList(List<EnderecoDoador> enderecos);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doador", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    EnderecoDoador toEntity(EnderecoDoadorDTO dto);

    // Atualizar entidade existente (para editar)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doador", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    void atualizarFromDTO(EnderecoDoadorDTO dto, @MappingTarget EnderecoDoador entidade);
}