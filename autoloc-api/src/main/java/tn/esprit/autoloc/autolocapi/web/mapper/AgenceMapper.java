package tn.esprit.autoloc.autolocapi.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgenceMapper {
    AgenceDto toDto(Agence agence);

    List<AgenceDto> toDtos(List<Agence> agences);

    @Mapping(target = "effectif", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    @Mapping(target = "employes", ignore = true)
    Agence toEntity(AgenceDto agenceDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idAgence", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    @Mapping(target = "employes", ignore = true)
    @Mapping(target = "effectif", ignore = true)
    void updateEntity(AgenceDto agenceDto, @MappingTarget Agence agence);

    List<Agence> toEntities(List<AgenceDto> agences);
}
