package tn.esprit.autoloc.autolocapi.web.dto;

/** API representation of an agency, without its JPA relationships. */
public record AgenceDto(
        Long idAgence,
        String nom,
        String ville,
        String adresse,
        String telephone,
        Integer effectif
) { }
