package tn.esprit.autoloc.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.autolocapi.domain.Agence;

public interface IAgenceRepository extends JpaRepository<Agence,Long> {
    @Query("select (count(v) > 0) from Vehicule v where v.agence.idAgence = :agenceId")
    boolean hasVehicules(@Param("agenceId") Long agenceId);
}
