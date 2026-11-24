package tn.esprit.autoloc.autolocapi.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    @Mapping(target = "agenceId", source = "agence.idAgence")
    VehiculeDto toDto(Vehicule vehicule);

    List<VehiculeDto> toDtos(List<Vehicule> vehicules);
}
